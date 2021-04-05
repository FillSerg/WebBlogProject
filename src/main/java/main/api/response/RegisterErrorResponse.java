package main.api.response;


//@Data
public class RegisterErrorResponse extends RegisterResponse {
    public InnerRegisterErrorResponse errors;

    public RegisterErrorResponse(boolean result) {
        super(result);
        this.errors = new InnerRegisterErrorResponse();
    }

    public InnerRegisterErrorResponse getErrors() {
        return errors;
    }

    public void setErrors(InnerRegisterErrorResponse errors) {
        this.errors = errors;
    }

    //    @Data
    public class InnerRegisterErrorResponse {
        private String email;
        private String name;
        private String password;
        private String captcha;

        public InnerRegisterErrorResponse() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    }
}