<template>
  <video autoplay />
</template>

<script>
export default {
  name: 'OvVideo',
  props: {
    streamManager: Object,
  },
  mounted() {
    if (this.streamManager) this.streamManager.addVideoElement(this.$el);
    //this.streamManager.createVideoElement(this.$el, 'REPLACE');
    console.log('%cOvVideo.vue line:13 this.$el', 'color: #007acc;', this.$el);
  },
};
</script>
