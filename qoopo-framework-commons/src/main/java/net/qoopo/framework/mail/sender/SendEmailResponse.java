package net.qoopo.framework.mail.sender;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author alberto
 */
@Getter
@Setter
@EqualsAndHashCode
public class SendEmailResponse implements Serializable {

    private boolean enviado;
    private String mensaje;

}
