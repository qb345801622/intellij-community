// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.kotlin.idea.statistics

import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.events.EventFields
import com.intellij.internal.statistic.service.fus.collectors.CounterUsagesCollector
import com.intellij.openapi.project.Project

object KotlinCodeVisionUsagesCollector : CounterUsagesCollector() {
    override fun getGroup(): EventLogGroup = GROUP

    const val CLASS_LOCATION = "class"
    const val INTERFACE_LOCATION = "interface"
    const val FUNCTION_LOCATION = "function"
    const val PROPERTY_LOCATION = "property"

    private val GROUP = EventLogGroup("kotlin.code.vision", 2)

    private val LOCATION_FIELD = EventFields.String(
        "location",
        listOf(CLASS_LOCATION, INTERFACE_LOCATION, FUNCTION_LOCATION, PROPERTY_LOCATION)
    )

    private val USAGES_CLICKED_EVENT = GROUP.registerEvent("usages.clicked")
    private val INHERITORS_CLICKED_EVENT = GROUP.registerEvent("inheritors.clicked", LOCATION_FIELD)
    private val SETTINGS_CLICKED_EVENT = GROUP.registerEvent("setting.clicked")
    private val CODE_AUTHOR_CLICKED_EVENT = GROUP.registerEvent("code.author.clicked", LOCATION_FIELD)

    fun logUsagesClicked(project: Project?) = USAGES_CLICKED_EVENT.log(project)
    fun logInheritorsClicked(project: Project?, location: String) = INHERITORS_CLICKED_EVENT.log(project, location)
    fun logSettingsClicked(project: Project?) = SETTINGS_CLICKED_EVENT.log(project)
    fun logCodeAuthorClicked(project: Project?, location: String) = CODE_AUTHOR_CLICKED_EVENT.log(project, location)
}
