package kr.hamburgersee.domain.review;

import kr.hamburgersee.domain.file.image.ReviewImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    @ResponseBody
    @PostMapping("/image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {

        String uploadedUrl = reviewImageService.uploadReviewImages(file);

        return uploadedUrl;
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new ReviewCreateForm());
        return "review-create";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute("form") ReviewCreateForm form,
            @RequestParam("allImageUrls") List<String> allImageUrls
    ) {
        Long reviewId = reviewService.write(form, null);

        reviewImageService.attachReview(reviewId, allImageUrls);
        reviewImageService.deleteUselessReviewImage(form.getContent(), allImageUrls);

        return "redirect:/";
    }
}

