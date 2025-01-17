package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("""
                
                
                
                """ +"========== Test Suite Başlıyor: {} ==========", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========== Test Suite Tamamlandı: {} ==========", context.getName());
        logger.info("Başarılı Test Sayısı: {}", context.getPassedTests().size());
        logger.info("Başarısız Test Sayısı: {}", context.getFailedTests().size());
        logger.info("========== Test Koşum Sonu: {} ==========", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test Başlıyor...");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Başarılı...");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test Başarısız: {} - Hata: {}", 
            result.getName(), 
            result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Atlandı: {}", result.getName());
    }
} 