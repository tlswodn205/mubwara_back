package site.metacoding.finals.dto.shop_table;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.finals.domain.shop.Shop;
import site.metacoding.finals.domain.shop_table.ShopTable;
import site.metacoding.finals.dto.repository.shop.QtyTableDto;
import site.metacoding.finals.dto.shop.ShopInnerDto.ShopDto;

public class ShopTableRespDto {
    @Setter
    @Getter
    public static class ShopTableListRespDto {
        private String maxPeople;
        private Integer qty;

        public ShopTableListRespDto(QtyTableDto dto) {
            this.maxPeople = dto.getMaxPeople();
            this.qty = dto.getQty();
        }

    }

    // 가게 테이블 저장
    @Setter
    @Getter
    public static class ShopTableSaveRespDto {
        private List<ShopTableDto> shopTableDtoList;

        @Setter
        @Getter
        public static class ShopTableDto {
            private Long id;
            private int maxPeople;
            private ShopDto shop;

            public ShopTableDto(ShopTable shopTable) {
                this.id = shopTable.getId();
                this.maxPeople = shopTable.getMaxPeople();
                this.shop = new ShopDto(shopTable.getShop());
            }

        }

        public ShopTableSaveRespDto(List<ShopTable> shopTableList) {
            this.shopTableDtoList = shopTableList.stream().map((shopTable) -> new ShopTableDto(shopTable))
                    .collect(Collectors.toList());
        }
    }

    // 가게 테이블 전체보기
    @Setter
    @Getter
    public static class AllShopTableRespDto {
        private List<ShopTableDto> shopTableDtoList;

        @Setter
        @Getter
        public static class ShopTableDto {
            private Long id;
            private int maxPeople;
            private int qty;
            private ShopDto shop;

            public ShopTableDto(ShopTable shopTable) {
                this.id = shopTable.getId();
                this.maxPeople = shopTable.getMaxPeople();
                this.shop = new ShopDto(shopTable.getShop());
            }

        }

        public AllShopTableRespDto(List<ShopTable> shopTableList) {
            this.shopTableDtoList = shopTableList.stream().map((shopTable) -> new ShopTableDto(shopTable))
                    .collect(Collectors.toList());
        }
    }
}
