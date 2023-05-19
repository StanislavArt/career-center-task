package pro.sky.jd9.careercentertask;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.jd9.careercentertask.controller.StockController;
import pro.sky.jd9.careercentertask.repository.SocksRepository;
import pro.sky.jd9.careercentertask.repository.StockRepository;
import pro.sky.jd9.careercentertask.service.StockService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StockControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SocksRepository socksRepository;

    @MockBean
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private final StockController stockController = new StockController(stockService);

    @Test
    public void getRemainderTest() throws Exception {
        String color = "red";
        int cottonPart = 40;
        int remainder = 15;


        Mockito.when(socksRepository.getRemainderByFilterIsEqual(color, cottonPart)).thenReturn(remainder);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("api/socks?color=" + color + "&operation=equal&cottonPart=" + cottonPart)
                        .accept(MediaType.TEXT_PLAIN))
                        .andExpect(status().isOk())
                        .andExpect(content().string(String.valueOf(remainder)));

    }

}
