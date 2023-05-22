package pro.sky.jd9.careercentertask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.jd9.careercentertask.controller.StockController;
import pro.sky.jd9.careercentertask.dto.WarehouseOperation;
import pro.sky.jd9.careercentertask.model.Socks;
import pro.sky.jd9.careercentertask.model.StockRecord;
import pro.sky.jd9.careercentertask.repository.SocksRepository;
import pro.sky.jd9.careercentertask.repository.StockRepository;
import pro.sky.jd9.careercentertask.service.StockService;
import pro.sky.jd9.careercentertask.service.StockServicePostgres;

import java.util.Optional;

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

    @SpyBean
    private StockServicePostgres stockService;

    @InjectMocks
    private StockController stockController;

    @Test
    public void getRemainderTest() throws Exception {
        String color = "red";
        int cottonPart = 40;
        int remainder = 15;


        Mockito.when(socksRepository.getRemainderByFilterIsEqual(color, cottonPart)).thenReturn(Optional.of(remainder));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color=" + color + "&operation=equal&cottonPart=" + cottonPart)
                        .accept(MediaType.TEXT_PLAIN))
                        .andExpect(status().isOk())
                        .andExpect(content().string(String.valueOf(remainder)));
    }

    @Test
    public void socksIncomeOutcomeTest() throws Exception {
        String color = "red";
        int cottonPart = 40;
        int quantity = 20;

        ObjectMapper objectMapper = new ObjectMapper();

        WarehouseOperation warehouseOperation = new WarehouseOperation();
        warehouseOperation.setColor(color);
        warehouseOperation.setCottonPart(cottonPart);
        warehouseOperation.setQuantity(quantity);

        Socks socks = new Socks();
        socks.setColor(color);
        socks.setCottonPart(cottonPart);

        StockRecord stockRecord = new StockRecord();
        stockRecord.setQuantity(quantity + 10);

        Mockito.when(socksRepository.findSocksByColorAndCottonPart(Mockito.any(String.class),
                                    Mockito.any(Integer.class))).thenReturn(Optional.of(socks));

        Mockito.when(stockRepository.findStockRecordBySocks(socks)).thenReturn(Optional.of(stockRecord));
        Mockito.when(stockRepository.save(stockRecord)).thenReturn(stockRecord);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/socks/income")
                .content(objectMapper.writeValueAsString(warehouseOperation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/socks/outcome")
                .content(objectMapper.writeValueAsString(warehouseOperation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
