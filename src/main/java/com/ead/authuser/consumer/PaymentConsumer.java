package com.ead.authuser.consumer;

import com.ead.authuser.enums.PaymentControl;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.model.dto.PaymentEventDto;
import com.ead.authuser.service.RoleService;
import com.ead.authuser.service.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {
    private final UserService userService;

    private final RoleService roleService;

    public PaymentConsumer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.paymentEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.paymentEvent}",
                    type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
    ))
    public void listenPaymentEvent(@Payload PaymentEventDto paymentEventDto) {
        UserModel userModel = userService.findById(paymentEventDto.getUserId()).get();
        var roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        switch (PaymentControl.valueOf(paymentEventDto.getPaymentControl())) {
            case EFFECTED: {
                if(userModel.getUserType().equals(UserType.USER)) {
                    userModel.setUserType(UserType.STUDENT);
                    userModel.getRoles().add(roleModel);
                    userService.updateUser(userModel);
                }
                break;
            }
            case REFUSED: {
                if(userModel.getUserType().equals(UserType.STUDENT)) {
                    userModel.setUserType(UserType.USER);
                    userModel.getRoles().remove(roleModel);
                    userService.updateUser(userModel);
                }
                break;
            }
            case ERROR: {
                System.out.println("Payment with ERROR.");
            }
        }
    }
}
