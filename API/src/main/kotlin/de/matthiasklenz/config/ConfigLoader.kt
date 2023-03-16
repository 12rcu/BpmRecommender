package de.matthiasklenz.config

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import java.io.File

class ConfigLoader {

    /**
     * Searches for the config:
     *
     * -> as a file in the run directory
     * -> as a file in a resource directory
     * -> as an Environment variable: "BPM_CONFIG"
     *
     * @return the Config of the API
     */
    fun loadConfig(): Config = createYamlParser().decodeFromString(loadConfigStr())

    private fun loadConfigStr(): String {
        return loadConfigFileFromResources()?.readText() ?: System.getenv("BPM_CONFIG")
        ?: throw Exception("No config provided!")
    }

    private fun loadConfigFileFromResources(): File? {
        return if (File("config.yaml").exists())
            File("config.yaml")
        else
            File(
                Thread.currentThread().contextClassLoader!!.getResource("config.yaml")?.path
                    ?: return null
            )
    }

    private fun createYamlParser(): Yaml {
        val yamlConfig = Yaml.default.configuration.copy(
            polymorphismStyle = PolymorphismStyle.Property,
            polymorphismPropertyName = "type",
        )
        return Yaml(Yaml.default.serializersModule, yamlConfig)
    }
}