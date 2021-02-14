<template>
	<div id="ask">
		<div class="container" id="contact">
			<div class="text-h4 text-center font-weight-black my-10 white--text">CONTACT US</div>
		</div>
		<div class="container">
			<div class="row">
				<div class="col-md-12 col-sm-12">
					<h3 class="h5 text-center mb-10 white--text">플랫폼 도입 문의</h3>
					<form action="" method="">
						<div class="row">

							<div class="col-lg-6 col-md-12 col-sm-12">
								<input class="bg-light form-control my-2" type="text" name="name" placeholder="*회사 이름" id="ask1"
									required="required" />
								<input class="bg-light form-control my-2" type="email" name="_replyto" placeholder="*이메일" id="ask2"
									required="required" />
								<input class="bg-light form-control my-2" type="text" name="name" placeholder="*전화번호" id="ask3"
									required="required" />
								<input class="bg-light form-control my-2" type="text" name="name" placeholder="*사용기간" id="ask4"
									required="required" />

							</div>
							<div class="col-lg-6 col-md-12 col-sm-12">
								<textarea class="bg-light mb-2 form-control my-1" name="message" placeholder="*비지니스 요구에 대한 추가 정보"
									rows="7" id="ask5" required="required"></textarea>
							</div>

						</div>


					<div class="row d-flex justify-center my-10">
						<v-btn x-large
              color="amber lighten-4"
               type="submit"
							style="width:200px"
							class="font-weight-black"
							@click="askForm('문의가 접수되었습니다. \n최대한 빠른 시일 내로 이메일 답변 드리겠습니다.\n감사합니다.',$event)">도입 문의</v-btn>
					</div>

					</form>


				</div>
			</div>
			<div class="row">
				<!-- <div class="col-md-12 col-sm-12 float-right text-right white--text"> -->
				<div class="col-md-12 col-sm-12 white--text text-center">
					<div class="m-3"><h3 class="h5 white--text font-weight-bold">Address</h3>
					<span>서울특별시 강남구 테헤란로 212<br />멀티캠퍼스 역삼<br />South Korea</span></div>

					<div class="m-3"><h3 class="h5 white--text font-weight-bold">Phone</h3>
					<p class="white--text">(+82)02-1212-1212</p></div>

					<div class="m-3"><h3 class="h5 white--text font-weight-bold">Email</h3>
					<p class="white--text">ssafytesta405@gmail.com</p></div>
				</div>
			</div>
		</div>



	</div>

</template>

<script>
	export default {
		name: "Ask",
		data: function () {
			return {
				images: {
					nightview : "@/assets/images/nightview.jpg"
				}

			}
		},

		methods: {
			askForm: function (message, event) {
				if (event) event.preventDefault();
				alert(message)
				document.getElementById("ask1").value = ''
				document.getElementById("ask2").value = ''
				document.getElementById("ask3").value = ''
				document.getElementById("ask4").value = ''
				document.getElementById("ask5").value = ''
			}


		},



		created: function () {

		},
		computed: {

		},
	}
</script>

<style scoped>

#ask {
	background-image: url("../../assets/images/nightview.jpg");
	/* height: 800px; */
}

</style>