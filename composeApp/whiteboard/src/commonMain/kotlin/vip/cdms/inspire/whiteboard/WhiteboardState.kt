package vip.cdms.inspire.whiteboard

import androidx.compose.runtime.Stable
import vip.cdms.inspire.whiteboard.utils.WhiteboardInitializer
import vip.cdms.inspire.whiteboard.utils.rememberWhiteboardState

/**
 * The state is used to control [InspireWhiteboard].
 *
 * @see rememberWhiteboardState
 */
@Stable
class WhiteboardState(initializer: WhiteboardInitializer) : WhiteboardInitializer by initializer {
    companion object
}
