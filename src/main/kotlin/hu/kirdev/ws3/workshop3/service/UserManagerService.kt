package hu.kirdev.ws3.workshop3.service

import com.fasterxml.jackson.databind.ObjectMapper
import hu.kirdev.ws3.workshop3.model.MapEntity
import hu.kirdev.ws3.workshop3.model.UserEntity
import jdk.jshell.spi.ExecutionControl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct

@Service
class UserManagerService {

    val userStorage = ConcurrentHashMap<String, UserEntity>()
    val maps = ConcurrentHashMap<String, MapEntity>()

    @Value("\${ws3.map-dir-name:sch}")
    lateinit var mapDirName: String

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @PostConstruct
    fun init() {
        val fileValue: String = ResourceUtils.getFile("classpath:maps/${mapDirName}/index.json").readText()
        val indexMap: MapEntity = objectMapper.readerFor(MapEntity::class.java).readValue(fileValue)
        maps["index"] = indexMap
    }


}