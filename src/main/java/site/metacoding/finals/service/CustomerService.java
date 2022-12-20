package site.metacoding.finals.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.finals.config.annotation.VerifyCustomer;
import site.metacoding.finals.config.auth.PrincipalUser;
import site.metacoding.finals.config.exception.RuntimeApiException;
import site.metacoding.finals.domain.customer.Customer;
import site.metacoding.finals.domain.customer.CustomerRepository;
import site.metacoding.finals.domain.option.Option;
import site.metacoding.finals.domain.option.OptionRepository;
import site.metacoding.finals.domain.reservation.Reservation;
import site.metacoding.finals.domain.reservation.ReservationRepository;
import site.metacoding.finals.domain.review.Review;
import site.metacoding.finals.domain.review.ReviewRepository;
import site.metacoding.finals.domain.shop.Shop;
import site.metacoding.finals.domain.shop.ShopQueryRepository;
import site.metacoding.finals.domain.shop.ShopRepository;
import site.metacoding.finals.domain.subscribe.SubscribeRepository;
import site.metacoding.finals.domain.user.User;
import site.metacoding.finals.domain.user.UserRepository;
import site.metacoding.finals.dto.customer.CustomerReqDto.CustomerJoinReqDto;
import site.metacoding.finals.dto.customer.CustomerReqDto.CustomerSaveReqDto;
import site.metacoding.finals.dto.customer.CustomerReqDto.CustomerUpdateReqDto;
import site.metacoding.finals.dto.customer.CustomerRespDto.CustoemrSaveRespDto;
import site.metacoding.finals.dto.customer.CustomerRespDto.CustomerJoinRespDto;
import site.metacoding.finals.dto.customer.CustomerRespDto.CustomerMyPageReviewRespDto;
import site.metacoding.finals.dto.customer.CustomerRespDto.CustomerMyPageSubscribeRespDto;
import site.metacoding.finals.dto.customer.CustomerRespDto.CustomerUpdateRespDto;
import site.metacoding.finals.dto.image_file.ImageFileReqDto.ImageHandlerDto;
import site.metacoding.finals.dto.repository.shop.ReservationRepositoryRespDto;
import site.metacoding.finals.dto.shop.ShopRespDto.ReservationShopRespDto;
import site.metacoding.finals.handler.ImageFileHandler;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRespository;
    private final ReservationRepository reservationRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public CustoemrSaveRespDto save(CustomerSaveReqDto customerSaveReqDto, PrincipalUser principalUser) {
        Customer customer = customerRepository.save(customerSaveReqDto.toEntity(principalUser.getUser()));

        return new CustoemrSaveRespDto(customer);
    }

    @Transactional
    public CustomerJoinRespDto join(CustomerJoinReqDto customerJoinReqDto) {
        String password = bCryptPasswordEncoder.encode(customerJoinReqDto.getPassword());
        customerJoinReqDto.setPassword(password);

        User user = userRepository.save(customerJoinReqDto.toUserEntity());
        Customer customer = customerRepository.save(customerJoinReqDto.toCustomerEntity(user));

        return new CustomerJoinRespDto(customer, user);
    }

    @VerifyCustomer
    @Transactional
    public CustomerUpdateRespDto update(PrincipalUser principalUser, CustomerUpdateReqDto customerUpdateReqDto) {
        // 검증 = aop
        Customer customerPS = principalUser.getCustomer();

        customerPS.updateCustomer(customerUpdateReqDto);
        customerRepository.save(customerPS);

        return new CustomerUpdateRespDto(customerPS);
    }

    @Transactional
    public void delete(PrincipalUser principalUser) {
        // 검증 = aop

        // 유저 삭제
        userRepository.deleteById(principalUser.getUser().getId());

        // 구독 정보는 바로 삭제
        if (subscribeRepository.findByCustomerId(principalUser.getCustomer().getId()).size() != 0) {
            subscribeRepository.deleteByCustomerId(principalUser.getCustomer().getId());
        }
        // 예약 정보는 소프트 딜리트
        List<Reservation> reservationPS = reservationRepository.findByCustomerId(principalUser.getCustomer().getId());
        if (reservationPS != null) {
            reservationPS.stream().forEach(r -> reservationRepository.deleteById(r.getId()));
        }

        // 리뷰는 삭제하지 않음

    }

    @Transactional(readOnly = true)
    public List<ReservationShopRespDto> myPageReservation(Long id) {
        System.out.println(id);
        List<ReservationRepositoryRespDto> reservations = shopRespository.findResevationByCustomerId(id);
        if (reservations.size() == 0) {
            throw new RuntimeApiException("예약 목록이 없음", HttpStatus.NOT_FOUND);
        }
        System.out.println("디버그 이미지 : " + reservations.size());
        return reservations.stream().map((r) -> new ReservationShopRespDto(r)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerMyPageSubscribeRespDto> myPageSubscribe(Long id) {

        List<Shop> shops = shopRespository.findSubscribeByCustomerId(id);

        if (shops.size() == 0) {
            throw new RuntimeApiException("구독한 가게가 없음", HttpStatus.NOT_FOUND);
        }

        List<CustomerMyPageSubscribeRespDto> respDto = new ArrayList<>();
        shops.forEach((s) -> respDto.add(new CustomerMyPageSubscribeRespDto(s)));
        return respDto;
    }

    @Transactional(readOnly = true)
    public List<CustomerMyPageReviewRespDto> myPageReview(Long id) {
        List<Review> reviews = reviewRepository.findByCustomerId(id);
        if (reviews.size() == 0) {
            throw new RuntimeApiException("작성한 리뷰가 없음", HttpStatus.NOT_FOUND);
        }

        return reviews.stream().map((r) -> new CustomerMyPageReviewRespDto(r)).collect(Collectors.toList());
    }

}
