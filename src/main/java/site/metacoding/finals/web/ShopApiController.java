package site.metacoding.finals.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.finals.config.annotation.VerifyCustomer;
import site.metacoding.finals.config.annotation.VerifyShop;
import site.metacoding.finals.config.auth.PrincipalUser;
import site.metacoding.finals.dto.ResponseDto;
import site.metacoding.finals.dto.repository.shop.AnalysisDto;
import site.metacoding.finals.dto.reservation.ReservationReqDto.AnalysisDateReqDto;
import site.metacoding.finals.dto.reservation.ReservationRespDto.AnalysisWeekRespDto;
import site.metacoding.finals.dto.shop.ShopReqDto.OptionListReqDto;
import site.metacoding.finals.dto.shop.ShopReqDto.ShopSaveReqDto;
import site.metacoding.finals.dto.shop.ShopReqDto.ShopUpdateReqDto;
import site.metacoding.finals.dto.shop.ShopRespDto.OptionListRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.PriceListRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ShopDetailRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ShopInfoSaveRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ShopListRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ShopLocationListRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ShopPopularListRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ShopSearchListRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ShopUpdateRespDto;
import site.metacoding.finals.service.ShopService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShopApiController {

    private final ShopService shopService;

    // shop???????????? ???????????? ?????? ??????

    // shop ??? ??? ??? ???????????? ?????? / respDto LAZY ?????? ???????????? ??? ??? ????????? ??????????????? ???
    @VerifyCustomer
    @PostMapping(value = "/user/shop/save")
    public ResponseEntity<?> saveShop(@AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody ShopSaveReqDto shopSaveReqDto) {
        ShopInfoSaveRespDto respDto = shopService.save(shopSaveReqDto, principalUser);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.CREATED, "?????? ???????????? ??????", respDto),
                HttpStatus.CREATED);
    }

    @VerifyShop
    @GetMapping("/shop/update")
    public ResponseEntity<?> updatePageShop(@AuthenticationPrincipal PrincipalUser principalUser) {
        ShopUpdateRespDto respDto = shopService.updatePage(principalUser);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.ACCEPTED, "?????? ?????? ?????????", respDto),
                HttpStatus.OK);
    }

    @PutMapping("/shop/update")
    public ResponseEntity<?> updateShop(@AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody ShopUpdateReqDto reqDto) {
        ShopUpdateRespDto respDto = shopService.update(reqDto, principalUser);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.ACCEPTED, "?????? ?????? ??????", respDto),
                HttpStatus.OK);
    }

    @VerifyShop
    @PostMapping("/shop/analysis/date")
    public ResponseEntity<?> dateAnalysis(@AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody AnalysisDateReqDto reqDto) {
        List<AnalysisDto> respDto = shopService.analysisDate(principalUser, reqDto);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "?????? ??????", respDto),
                HttpStatus.OK);
    }

    @VerifyShop
    @PostMapping("/shop/analysis/week")
    public ResponseEntity<?> weekAnalysis(@AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody AnalysisDateReqDto reqDto) {
        List<AnalysisWeekRespDto> respDtos = shopService.analysisWeek(principalUser, reqDto);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "?????? ??????", respDtos),
                HttpStatus.OK);
    }

    // customer???????????? ?????? ?????? ??????
    // ??????, ??????, ????????????, ???????????????, ??????
    // ?????? ?????? ?????? ?????? ?????? ???????????? ???
    @GetMapping("/list")
    public ResponseEntity<?> listShop() {
        List<ShopListRespDto> shopList = shopService.List();
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "?????? ?????? ????????? ??????", shopList), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> shopDetail(@PathVariable Long id, HttpServletRequest request) {
        String header = request.getHeader("Authentication");

        PrincipalUser principalUser = null;
        if (header != null) {
            System.out.println("?????? ?????? ????????????");
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            principalUser = (PrincipalUser) principal;
        }

        ShopDetailRespDto dto = shopService.detatil(id, principalUser);
        System.out.println(dto.getImageFile().getImage());
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "?????? ???????????? ??????", dto), HttpStatus.OK);
    }

    @GetMapping("/list/{categoryName}")
    public ResponseEntity<?> shopCategoryList(@PathVariable String categoryName) {
        List<ShopListRespDto> shopList = shopService.categoryList(categoryName);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "??????????????? ?????? ????????? ??????", shopList), HttpStatus.OK);
    }

    @GetMapping("/list/popular")
    public ResponseEntity<?> shopPopularList() {
        List<ShopPopularListRespDto> respDtos = shopService.popularList();
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "????????? ?????? ????????? ??????", respDtos), HttpStatus.OK);
    }

    @PostMapping("/list/option")
    public ResponseEntity<?> shopOptionList(@RequestBody List<OptionListReqDto> reqDto) {
        List<OptionListRespDto> respDtos = shopService.optionList(reqDto);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "???????????? ?????? ????????? ??????", respDtos),
                HttpStatus.OK);
    }

    @GetMapping("/list/price/{value}")
    public ResponseEntity<?> shopPriceList(@PathVariable("value") String value) {
        List<PriceListRespDto> respDtos = shopService.priceList(value);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "????????? ?????? ????????? ??????", respDtos), HttpStatus.OK);
    }

    @GetMapping("/list/search/{keyword}")
    public ResponseEntity<?> shopSearchList(@PathVariable("keyword") String keyword) {
        List<ShopSearchListRespDto> respDtos = shopService.searchList(keyword);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "?????? ????????? ??????", respDtos), HttpStatus.OK);
    }

    @GetMapping("/list/location") // /list/location?city=&region=
    public ResponseEntity<?> shopLocationList(@RequestParam(name = "city") String city,
            @RequestParam("region") String region) {
        System.out.println("1");
        System.out.println("????????? ??????????????? : " + city);
        List<ShopLocationListRespDto> respDtos = shopService.locationList(city, region);
        return new ResponseEntity<>(new ResponseDto<>(HttpStatus.OK, "?????? ????????? ??????", respDtos), HttpStatus.OK);
    }

}
