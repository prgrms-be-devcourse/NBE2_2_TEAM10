<<<<<<<< HEAD:src/main/java/com/prgrms2/java/bitta/user/exception/UserTaskException.java
package com.prgrms2.java.bitta.user.exception;

========
package com.prgrms2.java.bitta.feed.exception;
>>>>>>>> refs/remotes/origin/main:src/main/java/com/prgrms2/java/bitta/feed/exception/FeedTaskException.java

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FeedTaskException extends RuntimeException {
    private int code;
    private String message;
}
