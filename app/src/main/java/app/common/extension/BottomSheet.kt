package app.common.extension

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

inline fun BottomSheetBehavior<View>.expanded() {
    state = BottomSheetBehavior.STATE_EXPANDED
}

inline fun BottomSheetBehavior<View>.collapsed() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}
inline fun BottomSheetBehavior<View>.hide() {
    state = BottomSheetBehavior.STATE_HIDDEN
}