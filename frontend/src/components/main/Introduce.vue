<template>
	<div id="introduce">
		<!-- What is VieW -->
		<div data-aos="flip-right" class="headline text-center" style="margin-bottom: 10rem">WieV는 비대면 면접에 최적화된 솔루션을 제공하는 플랫폼입니다.</div>
		
		<!-- What we serve -->
		<v-container style="margin-bottom: 10rem">
			<v-row no-gutters>
				<div class="text-h4 text-center font-weight-black" style="margin-bottom: 5rem" data-aos="flip-left">What We Serve</div>
				<v-col v-for="(item, i) in serve" :key="i" class="serve-content">
					<v-card class="mx-auto my-12" max-width="374" elevation="0">

						<v-card-title class="text-h4 justify-center" data-aos="fade-down">{{ item.title }}</v-card-title>

						<v-icon size="90" color="#0277BD" style="margin: 3.5rem 0; width: 100%" data-aos="fade-down"> mdi-{{ item.icon }} </v-icon>

						<v-card-text>
							<div class="body-2 text-dark font-weight-medium" data-aos="fade-down">
								{{ item.content }}
							</div>
						</v-card-text>

						<!-- <v-divider class="mx-4"></v-divider> -->

					</v-card>
				</v-col>
			</v-row>
		</v-container>

		<!-- What we support -->
		<div class="text-h4 text-center font-weight-black" style="margin-bottom: 7rem" data-aos="flip-left">What We Support</div>
		<v-row style="margin-bottom: 10rem" no-gutters>
			<v-col v-for="(sup, i) in support" :key="i" cols="4" class="pa-0" data-aos="zoom-in">
				<v-hover v-slot="{ hover }">
					<v-card :class="{'on-hover': hover}" :dark="!hover" tile>
						<v-img :src="sup.src" class="support">
							<v-card-title class="headline">{{ sup.title }}</v-card-title>
							<v-card-subtitle>{{ sup.content }}</v-card-subtitle>
						</v-img>
					</v-card>
				</v-hover>
			</v-col>
		</v-row>

		<!-- What we deliver -->
		<div class="text-h4 text-center font-weight-black" style="margin-bottom: 7rem" data-aos="flip-left">What We Deliver</div>
		<v-carousel
			cycle
			height="300"
			hide-delimiter-background
			show-arrows-on-hover
			style="margin-bottom: 10rem"
		>
			<v-carousel-item v-for="(item, i) in carousel" :src="item.src" :key="i" active-class="crs-img">
				<v-sheet class="crs-content d-flex flex-column justify-center align-center" height="100%">
          <div class="text-h4 m-5">
            " {{ item.content }} "
          </div>
          <div class="text-h6">
            {{ item.subcontent }}
          </div>
        </v-sheet>
			</v-carousel-item>
		</v-carousel>

		<!-- Our Team -->
		<div class="text-h4 text-center font-weight-black" style="margin-bottom: 7rem" data-aos="flip-left">Our Team</div>
    <v-container style="margin-bottom: 10rem">
      <v-row class="mt-10">
        <v-col cols="12 d-flex justify-space-around">
          <div v-for="(member, i) in team" :key="i">
            <v-avatar size="200" class="avatarImg" data-aos="flip-right">
              <img alt="user" :src="member.img">
            </v-avatar>

            <p class="text-center h3 font-weight-black mt-3" data-aos="flip-right">{{ member.name }}</p>
            <p class="text-center h6 font-weight-black" data-aos="flip-right">{{ member.position }}</p>

            <div class="d-flex justify-center">
							<v-btn class="mx-2" fab dark small color="secondary" :href="member.github" target="_blank" data-aos="flip-right">
								<v-icon style="color:white">
									mdi-github
								</v-icon>
							</v-btn>
							<v-btn class="mx-2" fab dark small color="blue" :href="member.facebook" target="_blank" data-aos="flip-right">
								<v-icon style="color:white">
									mdi-facebook
								</v-icon>
							</v-btn>
							<v-btn class="mx-2" fab dark small color="pink" :href="member.instagram" target="_blank" data-aos="flip-right">
								<v-icon style="color:white">
									mdi-instagram
								</v-icon>
							</v-btn>
            </div>
          </div>
        </v-col>
      </v-row>
    </v-container>

		<!-- Contact -->
		<v-col>
			<!-- <div class="text-h4 text-center font-weight-black">Contact</div> -->
		</v-col>
  </div>
</template>

<script>
import AOS from 'aos'
import 'aos/dist/aos.css'

	export default {
		name: "Introduce",
		data: function () {
			return {
				isActive: false,

				serve: [
					{
						title: 'For Enterprise',
						icon: 'rotate-orbit',
						content: '효율적인 언택트 채용으로 면접 전형에서 발생하는 비용을 절감할 수 있습니다.'
					},
					{
						title: 'For Recruiter',
						icon: '',
						content: '비대면 면접스케줄을 통합 데이터로 관리하고 트래킹할 수 있습니다.'
					},
					{
						title: 'For Viewer',
						icon: '',
						content: '간편한 화면 구성과 핵심적인 기능들로 면접 자체에 집중할 수 있는 환경을 제공합니다.'
					},
				],

				support: [
					{
						title: '유연한 데이터베이스',
						content: '엑셀 업로드',
						detail: '',
						src: require('@/assets/images/support_img1.jpg')
					},
					{
						title: '통합 관리 시스템',
						content: '이메일 발송, 진행 중 면접',
						detail: '',
						src: require('@/assets/images/support_img1.jpg')
					},
					{
						title: '면접실 커스커마이징',
						content: '유형별, 데이터별 템플릿 활용',
						detail: '',
						src: require('@/assets/images/support_img1.jpg')
					},
					{
						title: '면접스케줄 자동화',
						content: '스케줄-면접실 연결',
						detail: '',
						src: require('@/assets/images/support_img1.jpg')
					},
					{
						title: '다양한 부가기능',
						content: '자기소개서, 공지 배너',
						detail: '',
						src: require('@/assets/images/support_img1.jpg')
					},
					{
						title: '',
						content: '웹 기반, 언제 어디서나',
						detail: '',
						src: require('@/assets/images/support_img1.jpg')
					}
				],

				carousel: [
					{
            src: require('@/assets/images/5-start-simple-slider.jpg'),
            content: "WieV는 '기업'에 초점을 맞춘 최고의 화상면접 플랫폼입니다.",
            subcontent:"실무자들이 면접의 목적인 지원자의 평가와 선발에 집중할 수 있도록 간편하고 효율적인 시스템을 제공합니다."
          },
          {
            src: require('@/assets/images/6-start-simple-slider.jpg'),
            content: "WieV는 성공적인 비즈니스를 운영할 수 있는 파트너입니다.",
            subcontent:"여러분이 오직 채용에만 집중할 수 있도록 모든 면접 일정을 생성 및 관리하겠습니다."
          },
          {
            src: require('@/assets/images/7-start-simple-slider.jpg'),
            content: "WieV는 언제 어디서나 면접을 진행하고 관리할 수 있습니다.",
            subcontent:"화상면접을 통해 지원자, 면접관을 쉽게 연결하며 채용에 대한 비용을 최소화합니다.",
          },
        ],

				team: [
					{
						img: require('@/assets/images/img5.jpg'),
						name: '정일규',
						position: 'Back',
						github: '',
						facebook: '',
						instagram: ''
					},
					{
						img: require('@/assets/images/img5.jpg'),
						name: '권영일',
						position: 'Back',
						github: '',
						facebook: '',
						instagram: ''
					},
					{
						img: require('@/assets/images/img5.jpg'),
						name: '김민정',
						position: 'Front',
						github: '',
						facebook: '',
						instagram: ''
					},
					{
						img: require('@/assets/images/img5.jpg'),
						name: '김윤지',
						position: 'Back',
						github: '',
						facebook: '',
						instagram: ''
					},
					{
						img: require('@/assets/images/img5.jpg'),
						name: '이준희',
						position: 'Front',
						github: '',
						facebook: '',
						instagram: ''
					}
				],

				transparent: 'rgba(255, 255, 255, 0)',
			}
		},

		methods: {
			
		},

		created: function () {
			AOS.init()
		},
		mounted() {
			AOS.refresh()
		},
		computed: {

		},
	}
</script>

<style scoped>
.serve-content {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	height: 30rem;
}
.x-expansion-enter-active {
  transition: all 5s ease;
}
/* .x-expansion-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
} */
.crs-content {
	background-color: transparent;
}
.crs-content::after {
	width: 100%;
	height: 100%;
	content: '';
	background-color: black;
	opacity: 0.5;
	position: absolute;
	top: 0;
	left: 0;
	z-index: -1;
}

.support::after {
  transition: background .3s ease-in-out;
	width: 100%;
	height: 100%;
	content: '';
	background-color: black;
	opacity: 0.8;
	position: absolute;
	top: 0;
	left: 0;
	z-index: -1;
}
.support:hover::after {
	transition: background .3s ease-in-out;
	background-color: #FFF1C3;
	font-weight: 400 !important;
  opacity: 0.8;
}


.nothover-gradient {
	background-image: linear-gradient(to top, rgba(0, 0, 0, .9), rgba(0, 0, 0, .9));
}
.hover-gradient {
	background-image: linear-gradient(to top, rgba(255, 255, 255, .9), rgba(255, 255, 255, .9));
}
.show-btns {
  color: rgba(255, 255, 255, 1) !important;
}

.avatarImg {
	border: 10px solid aliceblue;
}
</style>