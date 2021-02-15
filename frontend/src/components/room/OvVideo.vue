<template>
	<video class="myClass" autoplay/>
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
.myClass {
  height: inherit;
}
</style>
