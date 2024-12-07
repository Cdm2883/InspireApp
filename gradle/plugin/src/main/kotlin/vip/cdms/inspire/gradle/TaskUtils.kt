package vip.cdms.inspire.gradle

import org.gradle.api.Action
import org.gradle.api.Task

fun <T : Task> doLast(action: Action<T>) = Action<T> {
    doLast { action.execute(this@Action) }
}
