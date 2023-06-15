package airhacks.route53.boundary;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.constructs.Construct;

public class DomainCertificateStack extends Stack{
    
    private String domainName;
    private Certificate certificate;

    public DomainCertificateStack(Construct scope,String domainName) {
        super(scope,"airhacks-dns-certificate-stack");
        this.domainName = domainName;
        this.certificate = createCertificate();
    }

    Certificate createCertificate() {
        return Certificate.Builder.create(this, "DomainCertificate")
                .certificateName(this.domainName)
                .domainName("*.%s".formatted(this.domainName))
                .validation(CertificateValidation.fromDns())
                .build();
    }

    public Certificate getCertificate(){
        return this.certificate;
    }

}
