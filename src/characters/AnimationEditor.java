package characters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import display.Sprite;
import display.Window;
import physics.CollisionAreas;
import physics.CollisionBox;
import physics.Vector;

public class AnimationEditor {
	
	static List<Animation> animations;

	static JFrame frame;
	static JPanel previewWindow;
	static JPanel animsPanel;
	static JScrollPane animsScroller;
	static JList<Animation> anims;
	static DefaultListModel<Animation> animModel;
	static JButton addAnim;
	static JButton deleteAnim;
	static JCheckBox repeat;
	static JTextField name;
	static JPanel stepPanel;
	static JScrollPane stepScroller;
	static JList<AnimationStep> steps;
	static DefaultListModel<AnimationStep> stepModel;
	static JButton addStep;
	static JButton deleteStep;
	static JSlider angle;
	static JSpinner frameCount;
	static JCheckBox interuptable;
	static JCheckBox specialCancelable;
	static JCheckBox preview;
	static JPanel cBoxesPanel;
	static JScrollPane cBoxesScroller;
	static JList<CollisionBox> cBoxes;
	static DefaultListModel<CollisionBox> boxModel;
	static JButton addBox;
	static JButton deleteBox;
	static JSlider rotation;
	static JTextField boxName;
	static JSlider boxX;
	static JSlider boxY;
	static JSlider boxW;
	static JSlider boxH;
	static JButton color;
	static JCheckBox hitbox;
	static JSpinner damage;
	static JFileChooser imagePicker;
	static Color currColor;
	static JSpinner trajectoryX;
	static JSpinner trajectoryY;
	
	
	public static void main(String args[]) {
		frame = new JFrame();
		frame.getContentPane().setLayout(new GridLayout(1, 4));
		
		animsPanel = new JPanel(new BorderLayout());
		animModel = new DefaultListModel<>();
		anims = new JList<>(animModel);
		anims.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		anims.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (anims.isSelectionEmpty()) {
						resetAnimVals();
					} else {
						selectAnim(anims.getSelectedValue());
					}
				}
			}
		});
		animsScroller = new JScrollPane(anims);
		animsPanel.add(animsScroller, BorderLayout.CENTER);
		JPanel p = new JPanel(new GridLayout(0, 2));
		addAnim = new JButton("+");
		addAnim.setToolTipText("Add animation");
		addAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(frame, "Name of animation");
				if (name == null) {
					return;
				}
				animModel.addElement(new Animation(name, false, new ArrayList<AnimationStep>()));
				anims.setSelectedIndex(animModel.size() - 1);
				selectAnim(anims.getSelectedValue());
			}
		});
		p.add(addAnim);
		deleteAnim = new JButton("-");
		deleteAnim.setToolTipText("Delete animation");
		deleteAnim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animModel.remove(anims.getSelectedIndex());
				resetAnimVals();
			}
		});
		p.add(deleteAnim);
		name = new JTextField("Name");
		name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!anims.isSelectionEmpty()) {
					anims.getSelectedValue().setString(name.getText());
					anims.repaint();
				}
			}
		});
		p.add(name);
		repeat = new JCheckBox("Repeat");
		repeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!anims.isSelectionEmpty()) {
					anims.getSelectedValue().setRepeat(repeat.isSelected());
				}
			}
		});
		p.add(repeat);
		preview = new JCheckBox("Preview");
		preview.addActionListener(new ActionListener() {
			boolean savedVal;
			
			public void actionPerformed(ActionEvent e) {
				if (preview.isSelected()) {
					savedVal = anims.getSelectedValue().repeat();
					anims.getSelectedValue().setRepeat(true);
				} else {
					anims.getSelectedValue().setRepeat(savedVal);
				}
			}
		});
		p.add(preview);
		animsPanel.add(p, BorderLayout.SOUTH);
		frame.getContentPane().add(animsPanel);
		
		stepPanel = new JPanel(new BorderLayout());
		stepModel = new DefaultListModel<>();
		steps = new JList<>(stepModel);
		steps.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		steps.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (steps.isSelectionEmpty()) {
						resetStepVals();
					} else {
						selectStep(steps.getSelectedValue());
					}
				}
			}
		});
		stepScroller = new JScrollPane(steps);
		stepPanel.add(stepScroller, BorderLayout.CENTER);
		JPanel p2 = new JPanel(new GridLayout(0, 2));
		addStep = new JButton("+");
		addStep.setToolTipText("Add animation step");
		imagePicker = new JFileChooser("./sprites");
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "PNG Images", "png");
	    imagePicker.setFileFilter(filter);
		addStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (anims.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(frame, "You need to select an animation first!");
					return;
				}
				int returnVal = imagePicker.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					List<CollisionBox> boxes = new ArrayList<>();
					if (!steps.isSelectionEmpty()) {
						boxes.addAll(anims.getSelectedValue().getSteps().get(steps.getSelectedIndex()).getCollisions().getBoxes());
					}
					anims.getSelectedValue().getSteps()
							.add(new AnimationStep(new Sprite(imagePicker.getSelectedFile().getAbsolutePath()),
									interuptable.isSelected(), specialCancelable.isSelected(),
									(int) frameCount.getValue(), new CollisionAreas(boxes)));
					stepModel.addElement(
							anims.getSelectedValue().getSteps().get(anims.getSelectedValue().getSteps().size() - 1));
					steps.setSelectedIndex(stepModel.size() - 1);
					selectStep(steps.getSelectedValue());
				}
			}
		});
		p2.add(addStep);
		deleteStep = new JButton("-");
		deleteStep.setToolTipText("Delete animation step");
		deleteStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					anims.getSelectedValue().getSteps().remove(steps.getSelectedIndex());
					stepModel.remove(steps.getSelectedIndex());
					resetStepVals();
				}
			}
		});
		p2.add(deleteStep);
		interuptable = new JCheckBox("Interuptable");
		interuptable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					steps.getSelectedValue().setInteruptable(interuptable.isSelected());
				}
			}
		});
		p2.add(interuptable);
		specialCancelable = new JCheckBox("Special Cancelable");
		specialCancelable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!steps.isSelectionEmpty()) {
					steps.getSelectedValue().setSpecialInteruptable(specialCancelable.isSelected());
				}
			}
		});
		p2.add(specialCancelable);
		frameCount = new JSpinner(new SpinnerNumberModel(Animation.FRAMES_PER_SPRITE, 1, 6000, 1));
		frameCount.setToolTipText("Frames this step lasts");
		frameCount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!steps.isSelectionEmpty()) {
					steps.getSelectedValue().setFramesToDisplay((int) frameCount.getValue()); 
				}
			}
		});
		p2.add(frameCount);
		stepPanel.add(p2, BorderLayout.SOUTH);
		frame.getContentPane().add(stepPanel);
		
		cBoxesPanel = new JPanel(new BorderLayout());
		boxModel = new DefaultListModel<>();
		cBoxes = new JList<>(boxModel);
		cBoxes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cBoxes.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					if (cBoxes.isSelectionEmpty()) {
						resetBoxVals();
					} else {
						selectBox(cBoxes.getSelectedValue());
					}
				}
			}
		});
		cBoxesScroller = new JScrollPane(cBoxes);
		cBoxesPanel.add(cBoxesScroller, BorderLayout.CENTER);
		JPanel j3 = new JPanel(new GridLayout(0, 2));
		addBox = new JButton("+");
		addBox.setToolTipText("Add collision box");
		addBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (steps.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(frame, "You need to select an animation step first!");
					return;
				}
				String name = JOptionPane.showInputDialog(frame, "Name of hitbox");
				if (name == null) {
					return;
				}
				Double f1 = (Double) trajectoryX.getValue();
				Double f2 = (Double) trajectoryY.getValue();
				steps.getSelectedValue().getCollisions().getBoxes()
						.add(new CollisionBox(name, Color.YELLOW, boxX.getValue(),
								boxY.getValue(), boxW.getValue(), boxH.getValue(),
								rotation.getValue(), false, (int) damage.getValue(), new Vector(f1.floatValue(), f2.floatValue())));
				boxModel.addElement(steps.getSelectedValue().getCollisions().getBoxes()
						.get(steps.getSelectedValue().getCollisions().getBoxes().size() - 1));
				cBoxes.setSelectedIndex(boxModel.size() - 1);
				selectBox(cBoxes.getSelectedValue());
			}
		});
		j3.add(addBox);
		deleteBox = new JButton("-");
		deleteBox.setToolTipText("Delete collision box");
		deleteBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					anims.getSelectedValue().getSteps().get(steps.getSelectedIndex()).getCollisions().getBoxes()
							.remove(cBoxes.getSelectedIndex());
					boxModel.remove(cBoxes.getSelectedIndex());
					resetBoxVals();
				}
			}
		});
		j3.add(deleteBox);
		/*hitbox = new JCheckBox("Hitbox");
		hitbox.setToolTipText("Hitbox or Hurtbox");
		j3.add(hitbox);*/
		boxName = new JTextField("Name");
		boxName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					cBoxes.getSelectedValue().setName(boxName.getText());
					cBoxes.repaint();
				}
			}
		});
		j3.add(boxName);
		color = new JButton("Color");
		color.setToolTipText("Color of box");
		color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					Color newColor = JColorChooser.showDialog(
		                     frame, "Choose Color", currColor);
					if (newColor != null) {
						currColor = newColor;
						cBoxes.getSelectedValue().setColor(currColor);
					}
				}
			}
		});
		j3.add(color);
		boxX = new JSlider(0, 400, 110); //new JSpinner(new SpinnerNumberModel(50, 0, 1000, 1));
		boxX.setToolTipText("X Location");
		boxX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					cBoxes.getSelectedValue().setX(boxX.getValue()); 
				}
			}
		});
		j3.add(boxX);
		boxY = new JSlider(0, 400, 85); //new JSpinner(new SpinnerNumberModel(50, 0, 1000, 1));
		boxY.setToolTipText("Y Location");
		boxY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					cBoxes.getSelectedValue().setY(boxY.getValue()); 
				}
			}
		});
		j3.add(boxY);
		boxW = new JSlider(0, 400, 60); //new JSpinner(new SpinnerNumberModel(75, 0, 1000, 1));
		boxW.setToolTipText("Width");
		boxW.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					cBoxes.getSelectedValue().setWidth(boxW.getValue()); 
				}
			}
		});
		j3.add(boxW);
		boxH = new JSlider(0, 400, 25); //new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
		boxH.setToolTipText("Height");
		boxH.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					cBoxes.getSelectedValue().setHeight(boxH.getValue()); 
				}
			}
		});
		j3.add(boxH);
		rotation = new JSlider(0, 360, 0);//(new SpinnerNumberModel(0, 0, 359, 1));
		rotation.setToolTipText("Rotation (degrees)");
		rotation.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					cBoxes.getSelectedValue().setAngle(rotation.getValue()); 
				}
			}
		});
		j3.add(rotation);
		damage = new JSpinner(new SpinnerNumberModel(-1, -1, 1000, 1));
		damage.setToolTipText("Damage hitbox does");
		j3.add(damage);
		trajectoryX = new JSpinner(new SpinnerNumberModel(0, -25, 25, 0.25));
		trajectoryX.setToolTipText("Trajectory (X Component)");
		trajectoryX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					Double f = (Double) trajectoryX.getValue();
					cBoxes.getSelectedValue().getTrajectory().setX(f.floatValue()); 
				}
			}
		});
		j3.add(trajectoryX);
		trajectoryY = new JSpinner(new SpinnerNumberModel(0, -25, 25, 0.25));
		trajectoryY.setToolTipText("Trajectory (Y Component)");
		trajectoryY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!cBoxes.isSelectionEmpty()) {
					Double f = (Double) trajectoryY.getValue();
					cBoxes.getSelectedValue().getTrajectory().setY(f.floatValue()); 
				}
			}
		});
		j3.add(trajectoryY);
		cBoxesPanel.add(j3, BorderLayout.SOUTH);
		frame.getContentPane().add(cBoxesPanel);
		
		previewWindow = new JPanel() {			
			@Override
			public void paintComponent(Graphics g) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
				if (preview.isSelected() && !anims.isSelectionEmpty()) {
					anims.getSelectedValue().draw(0, 0, g);
					anims.getSelectedValue().advance();
				} else if (!steps.isSelectionEmpty()) {
					steps.getSelectedValue().draw(0, 0, g);
				}
			}
		};
		frame.getContentPane().add(previewWindow);
		frame.setPreferredSize(new Dimension(1000, 400));
		frame.pack();
		new Thread(new Runnable() {
			long lastDraw;
			
			@Override
			public void run() {
				while (true) {
					if (System.currentTimeMillis() - lastDraw > (1000 / Window.FRAMES_PER_SECOND)) {
						lastDraw = System.currentTimeMillis();
						previewWindow.repaint();
					}
				}
			}
			
		}).start();
		frame.setVisible(true);
	}
	
	public static void resetAnimVals() {
		name.setText("Name");
		repeat.setSelected(false);
		stepModel.clear();
		resetStepVals();
	}
	
	public static void resetStepVals() {
		interuptable.setSelected(false);
		specialCancelable.setSelected(false);
		frameCount.setValue(Animation.FRAMES_PER_SPRITE);
		boxModel.clear();
		resetBoxVals();
	}
	
	public static void resetBoxVals() {
		boxName.setText("Name");
		rotation.setValue(0);
		boxX.setValue(50);
		boxY.setValue(50);
		boxW.setValue(75);
		boxH.setValue(25);
		trajectoryX.setValue(0);
		trajectoryY.setValue(0);
	}
	
	public static void selectAnim(Animation anim) {
		name.setText(anim.toString());
		repeat.setSelected(anim.repeat());
		
		stepModel.clear();
		for (AnimationStep s : anim.getSteps()) {
			stepModel.addElement(s);
		}
		if (!stepModel.isEmpty()) {
			steps.setSelectedIndex(0);
			selectStep(stepModel.getElementAt(0));
		}
	}
	
	public static void selectStep(AnimationStep step) {
		interuptable.setSelected(step.isInteruptable());
		specialCancelable.setSelected(step.isSpecialInteruptable());
		frameCount.setValue(step.getFramesToDisplay());
		
		boxModel.clear();
		for (CollisionBox c : step.getCollisions().getBoxes()) {
			boxModel.addElement(c);
		}
		if (!boxModel.isEmpty()) {
			cBoxes.setSelectedIndex(0);
			selectBox(boxModel.getElementAt(0));
		}
	}
	
	public static void selectBox(CollisionBox box) {
		boxName.setText(box.toString());
		rotation.setValue((int) box.getAngle());
		boxX.setValue(box.getX());
		boxY.setValue(box.getY());
		boxW.setValue(box.getWidth());
		boxH.setValue(box.getHeight());
		trajectoryX.setValue((double) box.getTrajectory().getX());
		trajectoryY.setValue((double) box.getTrajectory().getY());
	}
}
