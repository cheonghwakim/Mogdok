<template lang="">
   <transition name="fade">
      <div v-show="isOpenAbout" class="about-modal">
         모달입니다
         <btn-underline :label="'닫기'" @onClick="closeAbout"></btn-underline>
      </div>
   </transition>
</template>
<script>
import BtnUnderline from '@/components/ui/BtnUnderline';

export default {
   components: { BtnUnderline },
   props: {
      isOpenAbout: {
         type: Boolean,
         default: false,
      },
   },

   methods: {
      closeAbout: function() {
         this.isOpenAbout = false;
      },
   },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.about-modal {
   position: fixed;
   width: 40vw;
   height: 80vh;
   background-color: rgb(255, 255, 255);
   box-shadow: 0px 9px 20px 0px #56565629;
   border-radius: 25px;
   z-index: 10;

   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);
}
</style>
