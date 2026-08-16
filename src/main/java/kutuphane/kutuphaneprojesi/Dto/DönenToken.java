package kutuphane.kutuphaneprojesi.Dto;

import lombok.Data;

@Data
public class DönenToken {
    private String token;
    public DönenToken (String token){
        this.token=token;
    }
}
