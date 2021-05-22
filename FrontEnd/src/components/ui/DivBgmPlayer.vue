<template lang="">
   <div class="div-bgm-player">
      <!-- 음악 플레이어 버튼 위치 -->
      <div v-wave class="btn-player-wrapper" @click="togglePlayer" :class="{ 'jello-vertical': !isPlaying && !isOpenPlayer }">
         <img v-show="!isOpenPlayer" src="@/assets/img/bgm-player/music.svg" alt="" />
         <btn-close class="btnClose" v-show="isOpenPlayer"></btn-close>
      </div>
      <!-- 컨트롤러 화면이 위치함 -->
      <transition name="player-open">
         <div v-show="isOpenPlayer" class="controller-wrapper">
            <div class="btn-wrapper" @click="toggleBGM">
               <svg-play v-show="!isPlaying"></svg-play>
               <svg-pause v-show="isPlaying"></svg-pause>
            </div>
            <div class="select-wrapper">
               <div v-for="(item, key) in bgmList" :key="key" class="bgm-select" @click="playBGM(key)" :class="{ selected: key === nowPlayIdx }">
                  <img :src="require(`@/assets/img/emoji/${item.imgSrc}.png`)" alt="" />
               </div>
            </div>
            <div class="volumn-wrapper">
               <input type="range" min="0" max="1" step="0.1" v-model="volume" />
            </div>
         </div>
      </transition>
   </div>
</template>
<script>
import SvgPlay from '@/components/svg/SvgPlay';
import SvgPause from '@/components/svg/SvgPause';
import BtnClose from '@/components/ui/BtnClose';

export default {
   components: {
      SvgPlay,
      SvgPause,
      BtnClose,
   },
   data() {
      return {
         // BGM 정보들 저장
         bgmList: [
            {
               title: '비 오는 소리',
               url: 'https://commitmentbucket.s3.ap-northeast-2.amazonaws.com/raining_sound.mp3',
               imgSrc: 'rain',
            },
            {
               title: '장작 타는 소리',
               url: 'https://commitmentbucket.s3.ap-northeast-2.amazonaws.com/fireplace_sound.mp3',
               imgSrc: 'fire',
            },
            {
               title: '시골 밤 귀뚜라미 소리',
               url: 'https://commitmentbucket.s3.ap-northeast-2.amazonaws.com/cricket_sound.mp3',
               imgSrc: 'camping',
            },
         ],

         // 플레이어 객체용
         audio: '',
         nowPlayIdx: 0,
         volume: 1,
         isPlaying: false,

         // 플레이어 컨트롤러 제어
         isOpenPlayer: false,
      };
   },
   mounted() {
      this.audio = new Audio(this.bgmList[this.nowPlayIdx].url);
      this.audio.loop = true;
      this.audio.volume = 1;
   },
   beforeDestroy() {
      this.audio.pause();
   },
   methods: {
      // 재생, 일시정지
      toggleBGM: function() {
         if (!this.isPlaying) {
            // 재생
            this.isPlaying = true;
            this.audio.load();
            this.audio.play();
         } else {
            this.isPlaying = false;
            this.audio.pause();
         }
      },

      // BGM 선택
      playBGM: function(idx) {
         this.nowPlayIdx = idx;
      },

      // 플레이어 닫기
      togglePlayer: function() {
         this.isOpenPlayer = !this.isOpenPlayer;
      },
   },
   watch: {
      nowPlayIdx() {
         // 다른 BGM 선택 시
         this.audio.src = this.bgmList[this.nowPlayIdx].url; // 파일 교체

         if (this.isPlaying) {
            // 원래 재생되고 있었다면 계속 재생
            this.isPlaying = false;
            this.toggleBGM();
         }
      },
      volume() {
         //볼륨조절
         this.audio.volume = this.volume;
      },
   },
};
</script>
<style scoped lang="scss">
.div-bgm-player {
   position: fixed;
   top: 70px;
   left: 30px;
   z-index: 20;

   /* 반응형 크기 */
   @media all and (max-width: 321px) {
      & {
         left: 10px;
      }
   }
}

.btn-player-wrapper {
   cursor: pointer;

   width: 45px;
   height: 45px;
   border-radius: 50%;
   background-color: rgb(255, 255, 255);
   box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   img {
      width: 50%;
      height: 50%;
   }

   .btnClose {
      width: 13px;
   }
}

/* 음악 재생 컨트롤러 */
.controller-wrapper {
   width: 300px;
   height: 45px;
   border-radius: 40px;
   background-color: rgb(255, 255, 255);
   box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);

   position: absolute;
   top: 0px;
   left: 55px;

   padding: 0px 20px;

   display: flex;
   align-items: center;
   justify-content: space-between;

   /* 반응형 크기 */
   @media all and (max-width: 400px) {
      & {
         width: 240px;
      }
   }

   /* 재생, 일시정지 버튼 */
   .btn-wrapper {
      /* padding-top: 5px; */
      width: 18px;
      height: 18px;
      /* background-color: red; */
   }

   /* 가운데 : BGM 테마 */
   .select-wrapper {
      width: 70%;
      height: 100%;

      display: flex;
      align-items: center;
      justify-content: center;

      .bgm-select {
         width: 20px;
         margin-right: 12px;
         cursor: pointer;

         img {
            width: 100%;
            -webkit-filter: grayscale(100%);
         }

         &.selected {
            /* background-color: red; */
            &::after {
               content: '';
               display: block;
               width: 5px;
               height: 5px;
               margin-top: 5px;
               margin-left: 8px;
               background-color: rgb(99, 99, 99);
               border-radius: 50%;
            }
            img {
               -webkit-filter: grayscale(0%);
            }
         }
      }
   }

   /* 마지막 : 볼륨 조절 */
   .volumn-wrapper {
      min-width: 80px;
      width: 40%;
      height: 100%;

      display: flex;
      align-items: center;
      justify-content: center;

      .close {
         width: 20px;
         margin-left: 15px;
      }

      /* 볼륨 재생 슬라이더 디자인 */
      input[type='range'] {
         width: 100%;
         -webkit-appearance: none;
         background: transparent;

         &:focus {
            outline: none;
         }

         &::-webkit-slider-thumb {
            -webkit-appearance: none;
            cursor: pointer;
            height: 20px;
            width: 20px;
            background-image: url('../../assets/img/bgm-player/vol_handle.svg');
            background-repeat: no-repeat;
            background-size: cover;
         }

         &::-webkit-slider-runnable-track {
            width: 100%;
            height: 17px;
            cursor: pointer;

            background-image: url('../../assets/img/bgm-player/vol_bar.svg');
            background-repeat: no-repeat;
            background-size: cover;
         }
      }
   }
}

.player-open-enter-active,
.player-open-leave-active {
   transition: all 0.3s ease;
}
.player-open-enter,
.player-open-leave-to {
   transform: translateX(-50px);
   opacity: 0;
}

.jello-vertical {
   -webkit-animation: jello-vertical 2s both;
   animation: jello-vertical 2s both;
   animation-iteration-count: 2;
}

@-webkit-keyframes jello-vertical {
   0% {
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
   }
   30% {
      -webkit-transform: scale3d(0.75, 1.25, 1);
      transform: scale3d(0.75, 1.25, 1);
   }
   40% {
      -webkit-transform: scale3d(1.25, 0.75, 1);
      transform: scale3d(1.25, 0.75, 1);
   }
   50% {
      -webkit-transform: scale3d(0.85, 1.15, 1);
      transform: scale3d(0.85, 1.15, 1);
   }
   65% {
      -webkit-transform: scale3d(1.05, 0.95, 1);
      transform: scale3d(1.05, 0.95, 1);
   }
   75% {
      -webkit-transform: scale3d(0.95, 1.05, 1);
      transform: scale3d(0.95, 1.05, 1);
   }
   100% {
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
   }
}
@keyframes jello-vertical {
   0% {
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
   }
   30% {
      -webkit-transform: scale3d(0.75, 1.25, 1);
      transform: scale3d(0.75, 1.25, 1);
   }
   40% {
      -webkit-transform: scale3d(1.25, 0.75, 1);
      transform: scale3d(1.25, 0.75, 1);
   }
   50% {
      -webkit-transform: scale3d(0.85, 1.15, 1);
      transform: scale3d(0.85, 1.15, 1);
   }
   65% {
      -webkit-transform: scale3d(1.05, 0.95, 1);
      transform: scale3d(1.05, 0.95, 1);
   }
   75% {
      -webkit-transform: scale3d(0.95, 1.05, 1);
      transform: scale3d(0.95, 1.05, 1);
   }
   100% {
      -webkit-transform: scale3d(1, 1, 1);
      transform: scale3d(1, 1, 1);
   }
}
</style>
