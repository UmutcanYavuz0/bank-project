package bank.project.Dto;

import lombok.Data;

@Data
public class DtoMoneyTransefer {
    private String senderaccountno;
    private int receiverid;
    private String receiveraccountno;
    private int amount;
    private String receiverusername;
}
