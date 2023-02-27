testModel:
	@echo "Downloading required packages...\n"
	pip install -r requirements.txt
	@echo "Downloading required packages... Done\n"
	@echo "Extracting Training Data...\n"
	python3 extract_training_data.py
	@echo "Classifying test symptoms Done\n"
	@echo "Training model...\n"
	python3 trainModel.py
	@echo "Training model... Done\n"
	@echo "Testing model...\n"
	python3 machineLearningTesting.py
