/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui.search

import tm.alashow.datmusic.data.DatmusicSearchParams
import tm.alashow.datmusic.domain.entities.Audio
import tm.alashow.domain.models.errors.ApiCaptchaError

internal sealed class SearchAction {
    data class QueryChange(val query: String = "") : SearchAction()
    object Search : SearchAction()
    data class SelectBackendType(val selected: Boolean, val backendType: DatmusicSearchParams.BackendType) : SearchAction()

    data class AddError(val error: Throwable) : SearchAction()
    object ClearError : SearchAction()
    data class SubmitCaptcha(val captchaError: ApiCaptchaError, val solution: String) : SearchAction()

    data class PlayAudio(val audio: Audio) : SearchAction()
}
