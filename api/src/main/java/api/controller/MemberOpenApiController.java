package api.controller;

import api.model.MemberLoginRequest;
import api.model.MemberLoginResponse;
import api.model.MemberRegisterRequest;
import api.model.MemberRegisterResponse;
import api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/member")
public class MemberOpenApiController {

    private final MemberService memberService;

    @PostMapping("/register")
    public MemberRegisterResponse register(@RequestBody MemberRegisterRequest request){
        MemberRegisterResponse response = memberService.register(request);
        return response;
    }

    @PostMapping("/sign-in")
    public MemberLoginResponse signIn(@RequestBody MemberLoginRequest request){
        return memberService.memberSignIn(request);
    }

}
