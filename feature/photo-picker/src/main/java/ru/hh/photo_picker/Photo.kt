package ru.hh.photo_picker


internal data class Photo(
    val url: String
) {
    companion object {
        val presets = listOf(
            Photo("https://gravatar.com/avatar/079e21a5f52feb805bfc8014f99c9e87?s=400&d=robohash&r=x"),
            Photo("https://gravatar.com/avatar/c998aae64c19900a29089b18ec64dd98?s=400&d=robohash&r=x"),
            Photo("https://gravatar.com/avatar/b50c10754e648e6b0d0ccc32c8020de7?s=400&d=robohash&r=x"),
            Photo("https://gravatar.com/avatar/2caca4a85a5c2e1de5836a9e89bc72e7?s=400&d=robohash&r=x")
        )
    }
}