#define TEST sample#include "PortabilityImpl.hh"#include "log4cpp/NDC.hh"#include "log4cpp/threading/Threading.hh"//instrumented function void NDC::setMaxDepth(int maxDepth) {
        getNDC()._setMaxDepth(maxDepth);
    }int main(){	int i = 0;	NDC::setMaxDepth(maxDepth);	return 0;}
