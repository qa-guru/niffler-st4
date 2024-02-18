package guru.qa.niffler.db.logging;

import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

public class JsonAttachment implements AttachmentData {

    private final String name;
    private final String json;

    public JsonAttachment(String name, String json) {
        this.name = name;
        this.json = json;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getJson() {
        return json;
    }

    public static void attachJson(String json) {
        JsonAttachment jsonAttachment = new JsonAttachment("JSON", json);
        new DefaultAttachmentProcessor().addAttachment(jsonAttachment, new FreemarkerAttachmentRenderer("json.ftl"));
    }

}
