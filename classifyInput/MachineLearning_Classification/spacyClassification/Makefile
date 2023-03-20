testModel:
	@echo "Downloading required packages...\n"
	pip install -r requirements.txt
	@echo "Downloading required packages... Done\n"
	@echo "Parsing RTF files...\n"
	python3 parse_rtf.py
	@echo "Parsing RTF files Done\n"
	@echo "Generating multi-labeled training data...\n"
	python3 generate_multilabels.py
	@echo "Generating multi-labeled training data... Done\n"
	@echo "Training model...\n"
	python3 trainMultiLabelModel.py
	@echo "Training model... Done\n"
	@echo "Testing model...\n"
	python3 Multi_MachineLearningTesting.py
