package utils

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

public class JSONUtil {

    static Object readJson(String filePath) {
        File file = new File(filePath)

        if (!file.exists()) {
            throw new IllegalArgumentException('JSON file not found: ' + filePath)
        }
        return new JsonSlurper().parse(file)
    }

    static String toPrettyJson(Object data) {
        return JsonOutput.prettyPrint(JsonOutput.toJson(data))
    }

    static void writeJson(Object data, String filePath) {
        String json = toPrettyJson(data)

        File file = new File(filePath)
        file.parentFile?.mkdir()

        file.withWriter('UTF-8') { writer ->
            writer << json
        }
    }

}
