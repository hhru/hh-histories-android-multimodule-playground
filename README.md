# android-multimodule-playground

Проект для демонстрации работы с межмодульной передачей зависимостей через 
[DI-фреймворк Toothpick](https://github.com/stephanenicolas/toothpick). 

Приложение состоит из 5 модулей:
- **app**
- **photo-picker** (feature-модуль) -- фича выбора фотографии пользователя
- **profile** (feature-модуль) -- фича профиля пользователя
- **di** (core-модуль) -- общие инструменты для построения DI
- **android-utils** (core-модуль) -- немного вспомогательных вещей для работы с Android-фреймворком

## Структура DI-скоупов

<img src="/img/scopes.png">

Скоупы делятся на два типа: **структурные** (выделены белым на картинке) и **присоединяемые** (выделены красным). 

- **структурные скоупы** -- описывают статичные связи между классами в приложении. 
Не имеют жизненного цикла, могут быть открыты в любой момент и не требуют закрытия.
Основное назначение - описывать связи для межмодульного взаимодействия.
Структурными скоупами являются root scope (aka app scope) и feature scope фичемодулей. 

- **присоединяемые скоупы** -- локальные скоупы, жизненный цикл которых эквивалентен жизненному циклу фрагментов. 
Присоединяемые скоупы на уровне фичи имеют иерархию идентичную вложенности фрагментов, но при этом их иерархии не пересекаются 
за пределами фичемодулей. 
Рутовый присоединяемый скоуп фичемодуля открывается от структурного feature scope. 

## Детали реализации

### DI-фреймворк

Для реализации DI в приложении используется Toothpick, но идея 1-к-1 реализуема и для других DI-фреймворков, поддерживающих 
ведение дерева и скоупинг DI-контейнеров.

### Межмодульная передача зависимостей

Для того, чтобы фичемодуль мог получить снаружи нужные ему `Deps`, необходимо реализовать для него `DepsImpl` в app-модуле
и предоставить байндинг в app scope. Пример:

```kotlin
// в feature-модуле
interface ProfileDeps {
    fun photoPickerFragment(profileId: String): Fragment
    fun photoSelections(profileId: String): Observable<String>
}

// в app-модуле
@InjectConstructor
internal class ProfileDepsImpl(
    // для реализации зависимостей фичемодуля, может понадобиться Api другого фичемодуля
    private val photoPickerApi: PhotoPickerApi
) : ProfileDeps {

    override fun photoPickerFragment(profileId: String): Fragment =
        photoPickerApi.photoPickerFragment(PhotoPickerArgs((profileId)))

    override fun photoSelections(profileId: String): Observable<String> =
        photoPickerApi.photoSelections()
            .filter { it.selectionId == profileId }
            .map { it.photo.url }

}

// в app-модуле, при описании DI-байндингов:
bind<ProfileDeps>().toClass<ProfileDepsImpl>()
bind<PhotoPickerApi>().toProviderInstance { ProfileFacade().api }
```

Здесь используется вспомогательный stateless-класс `FeatureFacade`, который является точкой входа в межмодульное взаимодействие для фичи. 
Этот класс обслуживает структурный Toothpick-скоуп фичи (feature scope), где хранится DI-байндинг для API фичи.  
Также FeatureFacade предоставляет доступ к feature scope как извне (из app-модуля), так и внутри модуля.

Пример объявления FeatureFacade внутри фичемодуля: 
```kotlin
class ProfileFacade : FeatureFacade<ProfileDeps, ProfileApi>(
    depsClass = ProfileDeps::class.java,
    apiClass = ProfileApi::class.java,
    featureScopeName = "ProfileFeature",
    featureScopeModule = {
        Module().apply {
            bind<ProfileApi>().singleton().releasable()
        }
    }
)
```

Пример использования FeatureFacade:
```kotlin
// Получение feature scope внутри фичи (например, для открытия скоупа фрагмента от него):
ProfileFacade().featureScope

// Получение API фичи в app-модуле (например, для последующего байндинга API в app scope):
ProfileFacade().api
```

### Локальный DI на уровне экранов (фрагментов)

Реализуется при помощи вспомогательного класса `DiFragmentPlugin`, который обеспечивает автоматическую связку 
DI-скоупа Toothpick и жизненного цикла фрагмента. DI-скоуп фрагмента переживает смену конфигурации.

Пример использования:

```kotlin
internal class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val di = DiFragmentPlugin(
        fragment = this,
        parentScope = { ProfileFacade().featureScope },
        scopeNameSuffix = { userProfile.id },
        scopeModules = { arrayOf(ProfileScreenModule(userProfile)) }
    )

    private val viewModel by lazy { di.get<ProfileViewModel>() }
    
    // ...
    
}
```

В скоупе фрагмента также будет заинсталлен примитив для отмены асинхронных операций (в данном приложении это `Disposable`
из Rx), который тоже связан с жизненным циклом фрагмента.
 
### ViewModel

В данном приложении *не используется* [ViewModel из AAC](https://developer.android.com/topic/libraries/architecture/viewmodel), 
так как `DiFragmentPlugin` уже предоставляет скоуп для переживания смены конфигурации и связанный с ним примитив для отмены асинхронных операций. 
Поэтому ViewModel в данном примере реализованы без наследования от базового класса. 
Для отмены подписок используется инжект `CompositeDisposable`, предоставляемый скоупом фрагмента.

### Реактивность

В данном примере используется [RxJava](https://github.com/ReactiveX/RxJava), но аналогичным образом реактивное взаимодействие 
и управление жизненным циклом подписок может бысть построено на основе [Flow](https://kotlinlang.org/docs/flow.html).

### Навигация

В приложении нет акцента на реализации навигации, используется простая передача фрагментов между фичемодулями. 
Но вместо методов `fun getExternalFragment(): Fragment` фичемодули могут объявлять в своих Deps метод в стиле
`fun openExternalScreen()`. Тогда app-модуль перенаправит обработку навигации в API фичемодуля, 
предоставляющего контейнер для навигации, где можно использовать любую навигационную библиотеку.