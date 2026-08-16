package kutuphane.kutuphaneprojesi.Controller;

import kutuphane.kutuphaneprojesi.Services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;
    @PostMapping("/search")
    public ArrayList<String> serach(@RequestParam String word){
        return searchService.search(word);
    }
}
