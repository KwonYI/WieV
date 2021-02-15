<template>
	<video class="video" autoplay/>
</template>

<script>
export default {
	name: 'OvVideo',

  data() {
    return {
    }
  },

	props: {
		streamManager: Object,
	},

	mounted () {
    console.log("이게 문젠가?", this.$el)
    this.streamManager.addVideoElement(this.$el);
	},
};
</script>
<style>
/* video.viewee {
  width: 350px;
  height: auto;
}
video.viewer {
  width: 350px;
  height: auto;
} */
.video {
  height: inherit;
}
</style>
