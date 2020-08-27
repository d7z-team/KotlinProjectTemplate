import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler

object Repository {

    fun loadMirrors(handler: RepositoryHandler, project: Project) {
        handler.run {
            mavenLocal()
            maven { url = project.uri("https://maven.aliyun.com/repository/public/") }
            jcenter()
            mavenCentral()
            maven { url = project.uri("https://jitpack.io") }
        }

    }
}

/**
 * INCLUDE OpenEDGN Project
 */
fun openEDGN(name: String, module: String? = null,version: String? = null): String{
    val versionStr = if (version!=null){
        ":$version"
    }else{
        ""
    }
    return if (module!=null){
        "com.github.OpenEdgn.$name:$module$versionStr"
    }else{
        "com.github.OpenEdgn:$name$versionStr"
    }
}
