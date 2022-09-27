package com.interview.task.urlchecker.api.config;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

/**
 * Запрос задающий список проверяемых узлов.
 */
public record UrlsRequest(@NotNull List<@URL String> urls) {}
