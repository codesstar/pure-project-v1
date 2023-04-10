<template>
  <div class="wrapper">
    <div
      style="margin: 100px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <!-- 这里ref要绑定一个userForm，然后在下面可以运用 -->
        <el-form-item prop="fname">
          <el-input size="medium" placeholder="请输入账号 " tyle="margin: 10px 0" prefix-icon="el-icon-user"
            v-model="user.fname"></el-input>
        </el-form-item>
        <el-form-item prop="remark">
          <el-input size="medium" placeholder="请输入密码 " style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
            v-model="user.remark"></el-input>
        </el-form-item>
        <el-form-item prop="confirmRemark">
          <el-input size="medium" placeholder="请确认密码 " style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
            v-model="user.confirmRemark"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="login">注册</el-button>
          <el-button type="warning" size="small" autocomplete="off">返回登陆</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
  
<script>
export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        fname: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        confirmRemark: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    login() {
      // 这里就是下面，要在这里校验，合不合法element-ui给你规定好了
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.remark !== this.user.confirmRemark) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')
              localStorage.setItem("user", JSON.stringify(res.data))
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>
  
<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  overflow: hidden;
}</style>