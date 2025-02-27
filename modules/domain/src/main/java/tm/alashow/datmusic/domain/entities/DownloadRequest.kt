/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDateTime
import tm.alashow.domain.models.BaseEntity

@Entity(tableName = "download_requests")
data class DownloadRequest(
    @ColumnInfo(name = "entity_id")
    val entityId: String = "",

    @ColumnInfo(name = "entity_type")
    val entityType: Type = Type.Audio,

    @ColumnInfo(name = "entity_json")
    val entity: String = "",

    @ColumnInfo(name = "request_id")
    val requestId: Int = REQUEST_NOT_SET,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = entityId,

    @ColumnInfo(name = "params")
    override var params: String = "",
) : BaseEntity {

    override fun getIdentifier() = id

    val audio
        get() = run {
            assert(entityType == Type.Audio)
            Json.decodeFromString(Audio.serializer(), entity)
        }

    companion object {
        const val REQUEST_NOT_SET = 0

        fun fromAudio(audio: Audio) = DownloadRequest(
            entityId = audio.id,
            entityType = Type.Audio,
            entity = Json.encodeToString(Audio.serializer(), audio)
        )
    }

    enum class Type {
        Audio;

        override fun toString() = name

        companion object {
            private val map = values().associateBy { it.name }

            fun from(value: String) = map[value] ?: Audio
        }
    }
}
