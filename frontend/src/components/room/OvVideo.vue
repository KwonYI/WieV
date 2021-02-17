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
  watch : {
    'streamManager' : 'changeDetect'
  },
  methods: {
    changeDetect(){
      this.streamManager.addVideoElement(this.$el);
    }
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
.myClass::after {
	width: 100%;
	height: 100%;
	content: '';
	background-color: black;
	opacity: 0.6;
	position: absolute;
	top: 0;
	left: 0;
	z-index: -1;
}
</style>
