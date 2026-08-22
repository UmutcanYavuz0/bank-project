package bank.project.Dto;

import lombok.Data;

@Data
public class DtoMoneyTransferByIban {
    private String senderaccountno;
    private String receiveriban;
    private int amount;
}
