<template>
	<video :class="{viewee: isViewee, viewer:!isViewee}" autoplay/>
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
video.viewee {
width: 350px; /* 여기 값 바꿔줘야된다 */ 
height: auto;
}
video.viewer {
width: 350px; /* 여기 값 바꿔줘야된다 */ 
height: auto;
}
.speaking{
  border-color: red;
  border-style: solid;
}
</style>
