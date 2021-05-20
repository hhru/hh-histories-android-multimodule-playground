package ru.hh.photo_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.hh.android_utils.args
import ru.hh.android_utils.bind
import ru.hh.android_utils.withArgs
import ru.hh.di.DiFragmentPlugin
import ru.hh.photo_picker.api.PhotoPickerFacade


internal class PhotoPickerFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(photoPickerArgs: PhotoPickerArgs) = PhotoPickerFragment().withArgs(photoPickerArgs)
    }

    private val photoPickerArgs by args<PhotoPickerArgs>()

    private val di = DiFragmentPlugin(
        fragment = this,
        parentScope = { PhotoPickerFacade().featureScope },
        scopeNameSuffix = { photoPickerArgs.selectionId },
        scopeModules = { arrayOf(PhotoPickerModule(photoPickerArgs)) }
    )

    private val viewModel by lazy { di.get<PhotoPickerViewModel>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind(viewModel.closeFragment) {
            dismiss()
        }

        view.findViewById<RecyclerView>(R.id.photo_recycler_view).apply {
            val photoAdapter = PhotoAdapter(viewModel::onPhotoSelected)
            adapter = photoAdapter
            layoutManager = GridLayoutManager(context, 3)
            photoAdapter.submitList(Photo.presets)
        }
    }

}