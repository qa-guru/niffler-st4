package guru.qa.niffler.db.logging;

import io.qameta.allure.attachment.AttachmentData;

public class SqlAttachment implements AttachmentData {

  private final String name;
  private final String sql;

  public SqlAttachment(String name, String sql) {
    this.name = name;
    this.sql = sql;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getSql() {
    return sql;
  }
}
