<template>
  <div id="introduce">
    <!-- What is VieW -->
    <div>WieV는 비대면 면접에 최적화된 솔루션을 제공하는 플랫폼입니다.</div>
    <!-- What we serve -->
    <v-container>
      <v-col>
        <div class="text-h4 text-center">What We Serve</div>
        <v-row no-gutters>
          <v-col class="d-flex flex-column align-center">
            <div class="text-h5">For Recruiter</div>
            <span>비대면 면접을 편리하게 관리할 수 있는 가치를 제공합니다.</span>
          </v-col>
          <v-col class="d-flex justify-center">
            <div class="text-h5">For Viewer</div>

          </v-col>
          <v-col class="d-flex justify-center">
            <div class="text-h5">For Viewee</div>

          </v-col>
        </v-row>
      </v-col>
    </v-container>

    <!-- What we support -->
    <div class="text-h4 text-center">What We Support</div>
    <v-row class="ma-0" no-gutters>
      <v-col v-for="(sup, i) in support" :key="i" cols="4" class="pa-0">
        <v-card color="grey" tile dark>
          <v-card-title class="headline">{{ sup.title }}</v-card-title>
          <v-card-subtitle>{{ sup.content }}</v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>

    <!-- What we deliver -->
    <div class="text-h4 text-center mt-10 mb-10 font-weight-black">What We Deliver</div>
    <v-carousel cycle height="300" hide-delimiter-background show-arrows-on-hover>
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
    <v-container class="mt-10 mb-10">
      <v-row>
        <div class="text-h4 text-center font-weight-black">Our Team</div>
      </v-row>

      <v-row class="mt-10">
        <v-col cols="12 d-flex justify-space-around">
          <div class="d-flex flex-column">
            <v-avatar size="200" class="avatarImg">
              <img alt="user" src="@/assets/images/img5.jpg">
            </v-avatar>
            <p class="text-center h3 font-weight-black mt-3">정일규</p>
            <p class="text-center h6 font-weight-black">BackEnd Developer</p>

            <div class="d-flex justify-center">
            <v-btn class="mx-2" fab dark small color="secondary" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-github
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="blue" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-facebook
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="pink" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-instagram
              </v-icon>
            </v-btn>
            </div>

          </div>



          <div>
            <v-avatar size="200" class="avatarImg">
              <img alt="user" src="@/assets/images/img5.jpg"></v-avatar>
              <p class="text-center h3 font-weight-black mt-3">권영일</p>
            <p class="text-center h6 font-weight-black">BackEnd Developer</p>

            <div class="d-flex justify-center">
            <v-btn class="mx-2" fab dark small color="secondary" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-github
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="blue" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-facebook
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="pink" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-instagram
              </v-icon>
            </v-btn>
            </div>
            
          </div>


          <div>
            <v-avatar size="200" class="avatarImg">
              <img alt="user" src="@/assets/images/img5.jpg"></v-avatar>
              <p class="text-center h3 font-weight-black mt-3">김민정</p>
            <p class="text-center h6 font-weight-black">FrontEnd Developer</p>
            <div class="d-flex justify-center">
            <v-btn class="mx-2" fab small color="secondary" href="https://github.com/snowcuphea" target="_blank" >
              <v-icon style="color:white">
                mdi-github
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="blue" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-facebook
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="pink" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-instagram
              </v-icon>
            </v-btn>
            </div>
            
          </div>

          <div>
            <v-avatar size="200" class="avatarImg">
              <img alt="user" src="@/assets/images/img5.jpg"></v-avatar>
              <p class="text-center h3 font-weight-black mt-3">김윤지</p>
           <p class="text-center h6 font-weight-black">BackEnd Developer</p>

            <div class="d-flex justify-center">
            <v-btn class="mx-2" fab dark small color="secondary" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-github
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="blue" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-facebook
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="pink" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-instagram
              </v-icon>
            </v-btn>
            </div>
            
          </div>

          <div>
            <v-avatar size="200" class="avatarImg">
              <img alt="user" src="@/assets/images/img5.jpg"></v-avatar>
              <p class="text-center h3 font-weight-black mt-3">이준희</p>
           <p class="text-center h6 font-weight-black">FrontEnd Developer</p>

            <div class="d-flex justify-center">
            <v-btn class="mx-2" fab dark small color="secondary" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-github
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="blue" href="https://github.com/snowcuphea" target="_blank">
              <v-icon style="color:white">
                mdi-facebook
              </v-icon>
            </v-btn>
            <v-btn class="mx-2" fab dark small color="pink" href="https://github.com/snowcuphea" target="_blank">
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
      <div class="text-h4 text-center">Contact</div>
    </v-col>


    <div class="container" id="work">
      <h2 class="text-h4 text-center">Our Service</h2>
    </div>

    <div class="container">
      <div class="row">
        <div class="col-md-4 col-sm-12 pt-2  d-flex justify-content-center">

          <v-card class="mx-auto my-12" max-width="374">
            <v-img height="250" src="@/assets/images/2-start-simple.jpg"></v-img>

            <v-card-title class="justify-center">Cafe Badilico</v-card-title>

            <v-card-text>

              <div class="my-4 subtitle-1">
                $ • Italian, Cafe
              </div>

              <div>Small plates, salads & sandwiches - an intimate setting with 12 indoor seats plus patio seating.
              </div>
            </v-card-text>

            <v-divider class="mx-4"></v-divider>

          </v-card>

        </div>
        <!-- <div class="card cardsize">
					<img class="img-fluid card-img-top cardimg" src="@/assets/images/2-start-simple.jpg"
						alt="2-start-simple" />
					<div class="card-body">
						<h3 class="h4">면접 유형에 따른 템플릿 제공</h3>
						<p class="text-muted">1:1 면접부터 그룹,PT 면접 등 면접 유형에 맞는 템플릿을 제공</p>
						<p>자기소개서, 이력서 보기를 통해 면접관에게 최적화된 면접 진행 가능</p>
					</div>
				</div> -->

        <!-- <div class="col-md-4 col-sm-12 pt-2 d-flex justify-content-center">
					<div class="card cardsize"><img class="img-fluid card-img-top cardimg" src="@/assets/images/unnamed2.jpg"
							alt="3-start-simple" />
						<div class="card-body">
							<h3 class="h4">면접 자동화 관리 시스템</h3>
							<p class="text-muted">면접 일정부터 면접 대상자 설정까지 자동으로 생성하는 ADMIN 페이지 제공</p>
							<p>
								엑셀 파일을 이용해 채용 데이터 저장 가능</p>
							<p>
								채용 담당자가 전체 면접 현황을 간편하게 관리
							</p>
						</div>
					</div>
				</div> -->

        <!-- <div class="col-md-4 col-sm-12 pt-2 d-flex justify-content-center">
					<div class="card cardsize"><img class="img-fluid card-img-top cardimg" src="@/assets/images/img4.png"
							alt="4-start-simple" />
						<div class="card-body">
							<h3 class="h4">화상 면접에 최적화된 환경 제공</h3>
							<p class="text-muted">별도의 설치 없이 웹 브라우저를 통해 사용</p>
							<p>시간과 장소의 제약없이 면접에만 집중 가능</p>
						</div>
					</div>
				</div> -->

      </div>
    </div>

    <section class="page-section mb-10" id="services">
      <div class="container my-5">
        <h3 class="h3 text-center">자동 채용 관리 시스템 & 실시간 화상 면접 을 통해</h3>
        <h3 class="h3 text-center">
          채용 편의성을 높이고 비즈니스에 집중할 수 있습니다.</h3>
      </div>
      <hr class="divider my-4" />
      <div class="container">
        <div class="row d-flex justify-content-center">
          <div class="col-lg-3 col-md-6 text-center">
            <div class="mt-5">
              <img src="@/assets/images/interview.png" width="150">
              <h3 class="h4 m-2">면접을 진행하고 싶은 사람이 있나요?</h3>
              <p class="text-muted mb-0">언제 어디서든 면접을 진행하세요.</p>
            </div>
          </div>
          <div class="col-lg-3 col-md-6 text-center">
            <div class="mt-5">
              <img src="@/assets/images/money-bag.png" width="150">
              <h3 class="h4 m-2">면접에 대한 비용을 절약하고 싶나요?</h3>
              <p class="text-muted mb-0">장소 대관에 들이는 시간 및 비용을 절약하세요.
                WieV가 알아서 제공해드립니다.</p>
            </div>
          </div>
          <div class="col-lg-3 col-md-6 text-center">
            <div class="mt-5">
              <img src="@/assets/images/calendar.png" width="150">
              <h3 class="h4 m-2">면접 일정을 쉽게 만들고 싶나요?</h3>
              <p class="text-muted mb-0">사내 채용 관련 데이터를 등록하면 자동으로 면접일정을 생성해 드립니다.</p>
            </div>
          </div>
        </div>
      </div>
    </section>



    <div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel">
      <ol class="carousel-indicators d-flex">
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-label="Slide 1">
        </li>
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" class="active" aria-label="Slide 2">
        </li>
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" class="active" aria-label="Slide 3">
        </li>
        <!-- <button class="active" data-target="#carouselExampleCaptions" data-slide-to="0"></button>
          <button data-target="#carouselExampleCaptions" data-slide-to="1"></button>
          <button data-target="#carouselExampleCaptions" data-slide-to="2"></button> -->
      </ol>
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img src="@/assets/images/5-start-simple-slider.jpg" class="d-block w-100 darkimg" alt="...">
          <div class="carousel-caption d-none d-md-block mb-10">
            <p class="text-h5">"WieV는 '기업'에 초점을 맞춘 최고의 화상면접 플랫폼입니다."</p>
            <p>실무자들이 면접의 목적인 지원자의 평가와 선발에 집중할 수 있도록 간편하고 효율적인 시스템을 제공합니다.</p>
          </div>
        </div>
        <div class="carousel-item">
          <img src="@/assets/images/6-start-simple-slider.jpg" class="d-block w-100 darkimg" alt="...">
          <div class="carousel-caption d-none d-md-block mb-10">
            <p class="text-h5">"WieV는 성공적인 비즈니스를 운영할 수 있는 파트너입니다."</p>
            <p>여러분이 오직 채용에만 집중할 수 있도록 모든 면접 일정을 생성 및 관리하겠습니다.</p>
          </div>
        </div>
        <div class="carousel-item">
          <img src="@/assets/images/7-start-simple-slider.jpg" class="d-block w-100 darkimg" alt="...">
          <div class="carousel-caption d-none d-md-block mb-10">
            <p class="text-h5">"WieV는 언제 어디서나 면접을 진행하고 관리할 수 있습니다."</p>
            <p>화상면접을 통해 지원자, 면접관을 쉽게 연결하며 채용에 대한 비용을 최소화합니다.</p>
          </div>
        </div>
      </div>
      <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions"
        data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions"
        data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>



  </div>

</template>

<script>
  export default {
    name: "Introduce",
    data: function () {
      return {
        support: [{
            title: '1번',
            content: '1번 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다.',
            detail: ''
          },
          {
            title: '2번',
            content: '2번 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다.',
            detail: ''
          },
          {
            title: '3번',
            content: '3번 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다.',
            detail: ''
          },
          {
            title: '4번',
            content: '4번 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다.',
            detail: ''
          },
          {
            title: '5번',
            content: '5번 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다.',
            detail: ''
          },
          {
            title: '6번',
            content: '6번 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다. 내용입니다.',
            detail: ''
          }
        ],

        carousel: [{
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
      }
    },

    methods: {


    },



    created: function () {

    },
    computed: {

    },
  }
</script>

<style scoped>
  .crs-content {
    background-color: transparent;
    /* background-color: ; */
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

  .avatarImg {
    border: 10px solid aliceblue;

  }
</style>