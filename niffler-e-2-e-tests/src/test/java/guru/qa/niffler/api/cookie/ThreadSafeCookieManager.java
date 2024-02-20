package guru.qa.niffler.api.cookie;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

public enum ThreadSafeCookieManager implements CookieStore {
  INSTANCE;

  private final ThreadLocal<CookieStore> tlCookieManager = ThreadLocal.withInitial(
      () -> new CookieManager(null, CookiePolicy.ACCEPT_ALL).getCookieStore()
  );

  private CookieStore getCookieStore() {
    return tlCookieManager.get();
  }

  @Override
  public void add(URI uri, HttpCookie cookie) {
    getCookieStore().add(uri, cookie);
  }

  @Override
  public List<HttpCookie> get(URI uri) {
    return getCookieStore().get(uri);
  }

  @Override
  public List<HttpCookie> getCookies() {
    return getCookieStore().getCookies();
  }

  @Override
  public List<URI> getURIs() {
    return getCookieStore().getURIs();
  }

  @Override
  public boolean remove(URI uri, HttpCookie cookie) {
    return getCookieStore().remove(uri, cookie);
  }

  @Override
  public boolean removeAll() {
    return getCookieStore().removeAll();
  }

  public String getCookieValue(String cookieName) {
    return getCookies()
        .stream()
        .filter(c -> c.getName().equals(cookieName))
        .map(HttpCookie::getValue)
        .findFirst()
        .orElseThrow();
  }
}
