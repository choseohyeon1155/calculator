package calculator;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator extends JFrame{
	
	// JTextField로 화면 구현
	private JTextField inputSpace;
	// 숫자와 연산자 구분을 위해 연산자 ArrayList 묶음
	private ArrayList<String> equation = new ArrayList<String>();
	// 계산식의 숫자를 담을 변수 num 생성
	private String num ="";
	// 클릭한 버튼을 기억하도록 변수 생성
	private String prev_operation = "";
	
	
	public Calculator() {
		// 기본으로 되어 있는 레이아웃 관리자 제거
		setLayout(null);
		
		///// 상단 값 인풋 영역 작업
		inputSpace = new JTextField();
		// 버튼으로만 조작하기 때문에 편집 가능 여부는 불가능으로 설정
		inputSpace.setEditable(false);
		// 배경색, 정렬 위치, 글씨체 설정
		inputSpace.setBackground(Color.WHITE);
		inputSpace.setHorizontalAlignment(JTextField.RIGHT);
		inputSpace.setFont(new Font("Arial", Font.BOLD, 50));
		inputSpace.setBounds(8, 10, 270, 70);
		
		///// 하단 버튼 영역 작업
		JPanel buttJPanel = new JPanel();
		buttJPanel.setLayout(new GridLayout(4, 4, 10, 10));
		buttJPanel.setBounds(8, 90, 270, 235);
		
		// 계산기 버튼의 글자를 차례대로 배열에 저장
		String button_names[] = {"C", "÷", "×", "=", "7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "0"};
		JButton buttons[] = new JButton[button_names.length];
		
		for(int i = 0; i < button_names.length; i++) {
			buttons[i] = new JButton(button_names[i]);
			buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
			if (button_names[i] == "C") buttons[i].setBackground(Color.RED);
			else if ((i >= 4 && i <= 6) || (i >= 8 && i <= 10) || (i >= 12 && i <= 14)) buttons[i].setBackground(Color.BLACK);
			else buttons[i].setBackground(Color.GRAY);
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setBorderPainted(false);
			// 버튼 클릭시 기능 부분 액션리스너 추가
			buttons[i].addActionListener(new PadActionListener());
			buttJPanel.add(buttons[i]);
		}
		
		// 화면에 출력&추가
		add(inputSpace);
		add(buttJPanel);
		
		// GUI 창의 제목, 사이즈, 보이기 여부 등 설정
		setTitle("계산기");
		setVisible(true);
		setSize(300, 370);
		// null 값으로 띄울 때 화면 가운데에 띄우며, 사이즈 조절 불가능하게 설정
		setLocationRelativeTo(null);
		setResizable(false);
		// CloseOperation : 창을 닫을 때 실행 중인 프로그램도 종료되게 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	// 버튼 선택 시 기능 추가
	class PadActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String operation = e.getActionCommand();
			
			if(operation.equals("C")) {
				inputSpace.setText("");
			} else if (operation.equals("=")) {
				String result = Double.toString(calculate(inputSpace.getText()));
				inputSpace.setText("" + result);
				num = "";
			} else if (operation.equals("+") || operation.equals("-") || operation.equals("÷") || operation.equals("×")) {  // 연산자 입력 방지 예외 처리
				if(inputSpace.getText().equals("") && operation.equals("-")) {
					// 값이 비어있을 때 "-"(음수) 입력 가능하게 처리
					inputSpace.setText(inputSpace.getText() + e.getActionCommand());
				} else if (!inputSpace.getText().equals("") && !prev_operation.equals("+") && !prev_operation.equals("-") && !prev_operation.equals("÷") && !prev_operation.equals("×")) {
					// 마지막으로 누른 버튼이 숫자인 경우 (비어있지 않고, 연산자가 아닐 때)
					inputSpace.setText(inputSpace.getText() + e.getActionCommand());
				}
			} else {
				inputSpace.setText(inputSpace.getText() + e.getActionCommand());
			}
			// 마지막으로 누른 버튼 기억할 수 있게 설정
			prev_operation = e.getActionCommand();
		}
	}
	
	// 계산 기능 구현을 위한 숫자와 연산 기호 구분
	private void fullTextParsing(String inputText) {
		equation.clear();
		
		for (int i = 0; i < inputText.length(); i++) {
			char ch = inputText.charAt(i);
			
			if (ch == '-' || ch =='+' || ch =='÷' || ch =='×') {
				equation.add(num);
				num ="";
				equation.add(ch + "");
			} else {
				num = num + ch;
			}
		}
		equation.add(num);
		equation.remove("");   // 음수("-") 입력 시 앞에 num="" 있기에 에러남 -> ""제거하여 처리
	}
	
	// 계산 기능
	public double calculate(String inputText) {
		fullTextParsing(inputText);
		
		double prev = 0;
		double current = 0;
		String mode = "";
		
		// 연산자 우선 처리
		for (int i = 0; i < equation.size(); i++) {
			String s = equation.get(i);
			
			if (s.equals("+")) {
				mode = "add";
			} else if (s.equals("-")) {
				mode = "sub";
			} else if (s.equals("×")) {
				mode = "mul";
			} else if (s.equals("÷")) {
				mode = "div";
			} else {
				// 전에 있던 연산자가 곱셈&나눗셈이고 현재 인덱스의 값이 숫자일 떄 연산처리
				if ((mode.equals("mul") || mode.equals("div")) && !s.equals("+") && !s.equals("-") && !s.equals("×") && !s.equals("÷")) {
					Double one = Double.parseDouble(equation.get(i - 2));
					Double two = Double.parseDouble(equation.get(i));
					Double result = 0.0;
					
					if(mode.equals("mul")) {
						result = one * two;
					} else if (mode.equals("div")) {
						result = one / two;
					}
					equation.add(i +1, Double.toString(result));
					
					for (int j = 0; j <3; j++) {
						equation.remove(i -2);
					}
					
					i -= 2;  // 결과값이 생긴 인덱스로 이동
				}
			}
		}   // 곱셈 나눗셈을 먼저 계산한다
		
		for (String s : equation) {
			if (s.equals("+")) {
				mode = "add";
			} else if (s.equals("-")) {
				mode = "sub";
			} else {
				current = Double.parseDouble(s);
				if (mode.equals("add")) {
					prev += current;
				} else if (mode.equals("sub")) {
					prev -= current;
				} else {
					prev = current;
				}
			}
			// 값이 소수점인 경우 소수점 아래 여섯자리만 노출되게 처리
			// Math.round(n*(10*표시할 자릿수))/10*표시할 자릿수
			prev = Math.round(prev * 100000) / 100000;
		}
		
		return prev;
	}
	
	
	public static void main(String[] args) {
		new Calculator();
	}
}
