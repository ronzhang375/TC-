package com.lottery.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 体彩衍生品商城小程序启动类
 */
@SpringBootApplication
@MapperScan("com.lottery.mall.generator.mapper")
public class LotteryMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryMallApplication.class, args);
        System.out.println("""

            ╔═══════════════════════════════════════════════════════════════╗
            ║                                                               ║
            ║     🎯 体彩衍生品商城小程序 - 服务启动成功                    ║
            ║                                                               ║
            ║     📖 API文档: http://localhost:8080/doc.html              ║
            ║                                                               ║
            ╚═══════════════════════════════════════════════════════════════╝
            """);
    }
}
