package gr.devian.talosquests.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Calendar;
import java.util.Date;


@SpringBootApplication
@EnableWebMvc
@EnableTransactionManagement
public class TalosQuestsBackendApplication {
	private static Calendar calendar = Calendar.getInstance();
	private static long startTime;
	public static Long getStartTime() {
		return startTime;
	}
	public static void main(String[] args) {
		SpringApplication.run(TalosQuestsBackendApplication.class, args);
		calendar.setTime(new Date());
		startTime = calendar.getTimeInMillis();
	}
	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
	public DispatcherServlet dispatcherServlet() {
		final DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setDispatchOptionsRequest(true);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return dispatcherServlet;
	}
	@Bean
	public ServletRegistrationBean dispatcherRegistration()
	{
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
		return registration;
	}
}
