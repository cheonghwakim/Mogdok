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
         this.$emit('onClick');
      },
   },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.about-modal {
   min-width: 300px;
   max-width: 600px;
   width: 80vw;
   height: 80vh;
   max-height: 900px;

   position: fixed;
   z-index: 111;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);

   background-color: rgb(255, 255, 255);
   box-shadow: 0px 9px 20px 0px #56565629;
   border-radius: 25px;
}
</style>
