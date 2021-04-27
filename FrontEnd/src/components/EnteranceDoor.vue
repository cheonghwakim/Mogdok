<template lang="">
   <div class="door">
      {{ this.room.roomTitle }}
   </div>
</template>
<script>
export default {
   props: {
      room: Object,
   },
};
</script>
<style scoped lang="scss">
.door {
   width: 150px;
   height: 200px;
   background-color: blue;
}
</style>
