import matlab.engine
eng = matlab.engine.start_matlab()

def execute(filename):
    ret = eng.inference(filename)
    return ret