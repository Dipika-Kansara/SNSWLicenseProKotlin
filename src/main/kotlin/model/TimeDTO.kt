package model

import kotlinx.serialization.Serializable
import java.time.Duration

@Serializable
class TimeUnitDTO {
    val hours: Long
    val minutes: Long
    val seconds: Long
    constructor(d: Duration){
        hours = d.seconds / 3600;
        minutes = (d.seconds  % 3600) / 60;
        seconds = d.seconds  % 60;
    }
}
