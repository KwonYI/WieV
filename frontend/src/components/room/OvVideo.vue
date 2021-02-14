<template>
	<video class="video" autoplay/>
</template>

<script>
export default {
	name: 'OvVideo',

  data() {
    return {
      isViewee : false,
    }
  },

	props: {
		streamManager: Object,
	},

	mounted () {
    let type = JSON.parse(this.streamManager.stream.connection.data.split('%/%')[0]).type
		if(type == 'viewee'){
      this.isViewee = true
    }
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
.speaking{
  border-color: red;
  border-style: solid;
}
.video {
  height: inherit;
}
</style>
