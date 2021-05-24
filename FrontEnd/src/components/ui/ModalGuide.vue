<template lang="">
   <transition name="fade">
      <div v-show="isOpenGuide" class="guide-modal">
         <swiper class="swiper" :options="swiperOption">
            <swiper-slide v-for="(item, idx) in slides" :key="idx">
               <div class="slide" :style="{ 'background-image': 'url(' + item.src + ')' }"></div>
            </swiper-slide>
            <div class="swiper-pagination" slot="pagination"></div>
            <div class="swiper-button-prev" slot="button-prev"></div>
            <div class="swiper-button-next" slot="button-next"></div>
         </swiper>

         <btn-rounded class="btnClose" :label="'닫기'" :color="'green'" @onClick="closeAbout"></btn-rounded>
      </div>
   </transition>
</template>
<script>
import BtnRounded from '@/components/ui/BtnRounded';

// Swiper NPM 사용
import { Swiper, SwiperSlide } from 'vue-awesome-swiper';
import 'swiper/css/swiper.css';

export default {
   components: { BtnRounded, Swiper, SwiperSlide },
   props: {
      isOpenGuide: {
         type: Boolean,
         default: false,
      },
   },
   data() {
      return {
         slides: [
            {
               src: require('@/assets/img/guide/ms_guide_1.png'),
            },
            {
               src: require('@/assets/img/guide/ms_guide_2.png'),
            },
            {
               src: require('@/assets/img/guide/ms_guide_3.png'),
            },
            {
               src: require('@/assets/img/guide/ms_guide_4.png'),
            },
            {
               src: require('@/assets/img/guide/ms_guide_5.png'),
            },
            {
               src: require('@/assets/img/guide/ms_guide_6.png'),
            },
            {
               src: require('@/assets/img/guide/ms_guide_7.png'),
            },
         ],
         swiperOption: {
            slidesPerView: 1,
            spaceBetween: 30,
            loop: true,
            pagination: {
               el: '.swiper-pagination',
               type: 'progressbar',
            },
            navigation: {
               nextEl: '.swiper-button-next',
               prevEl: '.swiper-button-prev',
            },
         },
      };
   },

   methods: {
      closeAbout: function() {
         this.$emit('onClick');
      },
   },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.guide-modal {
   min-width: 300px;
   max-width: 600px;
   width: 80%;
   height: 80%;
   max-height: 900px;

   position: fixed;
   z-index: 111;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);

   background-color: rgb(255, 255, 255);
   box-shadow: 0px 9px 20px 0px #56565629;
   border-radius: 25px;

   padding: 20px;

   .swiper {
      height: 100%;
      width: 100%;
      /* border: 1px solid red; */

      .swiper-slide {
         height: 100%;
         width: 100%;
         padding-bottom: 25px;
         /* border: 1px solid rgb(0, 21, 255); */

         .slide {
            width: inherit;
            height: inherit;
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            /* border: 1px solid black; */
         }
      }

      .swiper-button-prev,
      .swiper-button-next {
         color: black;
      }
   }

   .btnClose {
      position: absolute;
      width: 200px;
      bottom: 20px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 111;

      &:hover {
         transform: translateX(-50%) translateY(-7px);
      }
   }
}
</style>
